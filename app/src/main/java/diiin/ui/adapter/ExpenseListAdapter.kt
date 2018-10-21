package diiin.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.Expense
import diiin.ui.activity.InsertExpenseActivity
import diiin.util.MathService
import diiin.util.MessageDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

interface ExpenseListAdapterContract {
    fun currentContext() : Context
    fun onRemoveItem()
}

/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseListAdapter(acontract: ExpenseListAdapterContract, alstExpenseList: ArrayList<Expense>)
    : RecyclerView.Adapter<ExpenseListAdapter.ExpenseListItemViewHolder>() {

    val mltExpenseList: ArrayList<Expense> = alstExpenseList
    private val mcontract: ExpenseListAdapterContract = acontract

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseListItemViewHolder {
        return ExpenseListItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_expenseslist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mltExpenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        val expenseItem = mltExpenseList[position]

        if (expenseItem.msValue != null)
            holder.tvValue.text = MathService.formatFloatToCurrency(expenseItem.msValue!!)


        holder.tvDate.text = expenseItem.mstrDate

        val nExpenseTypeId = expenseItem.mnExpenseType

        if (nExpenseTypeId != null && DindinApp.mhmExpenseType != null) {
            val expenseType = DindinApp.mhmExpenseType!![nExpenseTypeId]
            if (expenseType != null) {
                val nColor = Color.parseColor(expenseType.mstrColor)
                holder.vwExpenseType.setBackgroundColor(nColor)
                holder.tvExpenseDescription.text = expenseItem.mstrDescription
                holder.tvDescriptionDetailed.text = expenseType.mstrDescription
            }
        }

        holder.llLine2.visibility = LinearLayout.GONE

        if (mcontract.currentContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            holder.llLine2.visibility = LinearLayout.VISIBLE

        holder.ivImageViewMenu.setOnClickListener { aview ->
            val popupMenu = PopupMenu(mcontract.currentContext(), aview)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ctxmenudelete -> {
                        MessageDialog.showMessageDialog(mcontract.currentContext(),
                                mcontract.currentContext().resources.getString(R.string.msgAreYouSure),
                                DialogInterface.OnClickListener { adialog, _ ->
                                    val expenseTarget: Expense = mltExpenseList[position]
                                    Observable.just(true).subscribeOn(Schedulers.io())
                                            .subscribe {
                                                DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseDao()?.delete(expenseTarget)
                                                mltExpenseList.removeAt(position)
                                                Observable.just(true)
                                                        .subscribeOn(AndroidSchedulers.mainThread())
                                                        .subscribe {
                                                            notifyItemRemoved(position)
                                                            mcontract.onRemoveItem()
                                                        }
                                            }
                                },
                                DialogInterface.OnClickListener { adialog, _ ->
                                    adialog.dismiss()
                                })
                        true
                    }
                    R.id.ctxmenuedit -> {
                        val intent = Intent(mcontract.currentContext(), InsertExpenseActivity::class.java)
                        val nExpenseId: Long? = mltExpenseList[position].mnID
                        intent.putExtra(InsertExpenseActivity.INTENT_KEY_EXPENSEID, nExpenseId)
                        mcontract.currentContext().startActivity(intent)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
    }

    class ExpenseListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val tvExpenseDescription: TextView = avwView.findViewById(R.id.tvExpenseType)
        val tvDescriptionDetailed: TextView = avwView.findViewById(R.id.tvDescription)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val vwExpenseType: View = avwView.findViewById(R.id.vwExpenseType)
        val llLine2: LinearLayout = avwView.findViewById(R.id.llLine2)
        val ivImageViewMenu: ImageView = avwView.findViewById(R.id.ivMenuOption)
    }
}