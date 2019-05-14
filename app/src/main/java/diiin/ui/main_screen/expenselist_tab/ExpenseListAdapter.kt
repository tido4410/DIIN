package diiin.ui.main_screen.expenselist_tab

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.Expense
import diiin.ui.insert_expense_screen.InsertExpenseActivity
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
                .inflate(R.layout.expense_item_card, parent, false))
    }

    override fun getItemCount(): Int {
        return mltExpenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseListItemViewHolder, position: Int) {
        val expenseItem = mltExpenseList[position]

        if (expenseItem.msValue != null)
            holder.tvValue.text = MathService.formatFloatToCurrency(expenseItem.msValue!!)

        val nExpenseTypeId = expenseItem.mnExpenseType

        if (nExpenseTypeId != null && DindinApp.mhmExpenseType != null) {
            val expenseType = DindinApp.mhmExpenseType!![nExpenseTypeId]
            if (expenseType != null) {
                val nColor = Color.parseColor(expenseType.mstrColor)
                holder.ivImageIcon.setColorFilter(nColor)
                holder.tvExpenseTitle.text = expenseItem.mstrDescription
            }
        }

        holder.ivImageViewMenu.setOnClickListener { aview ->
            val popupMenu = PopupMenu(mcontract.currentContext(), aview)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ctxmenudelete -> {
                        MessageDialog.showMessageDialog(mcontract.currentContext(),
                                mcontract.currentContext().resources.getString(R.string.msgAreYouSure),
                                DialogInterface.OnClickListener { _, _ ->
                                    val expenseTarget: Expense = mltExpenseList[position]
                                    Observable.just(true).subscribeOn(Schedulers.io())
                                            .subscribe {
                                                DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseDao()?.delete(expenseTarget)
                                                mltExpenseList.removeAt(position)
                                                Observable.just(true)
                                                        .subscribeOn(AndroidSchedulers.mainThread())
                                                        .subscribe { notifyItemRemoved(position) }
                                                mcontract.onRemoveItem()
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
        val tvExpenseTitle: TextView = avwView.findViewById(R.id.tvTitle)
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val ivImageViewMenu: ImageView = avwView.findViewById(R.id.ivMenuOption)
        val ivImageIcon : ImageView = avwView.findViewById(R.id.ivExpenseTypeIcon)
    }
}