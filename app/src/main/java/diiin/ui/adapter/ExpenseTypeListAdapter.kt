package diiin.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.ExpenseType
import diiin.ui.activity.InsertExpenseTypeActivity
import diiin.util.MessageDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ExpenseTypeListAdapterContract {
    fun currentContext() : Context
}

/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseTypeListAdapter(acontract: ExpenseTypeListAdapterContract, alstExpenseList: ArrayList<ExpenseType>)
    : RecyclerView.Adapter<ExpenseTypeListAdapter.ExpenseTypeListItemViewHolder>() {

    val mltExpenseTypeList: ArrayList<ExpenseType> = alstExpenseList
    private val mcontract: ExpenseTypeListAdapterContract = acontract

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseTypeListItemViewHolder {
        return ExpenseTypeListItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_expensetype_color_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mltExpenseTypeList.size
    }

    override fun onBindViewHolder(holder: ExpenseTypeListItemViewHolder, position: Int) {
        val expenseTypeItem = mltExpenseTypeList[position]
        holder.tvExpenseType.text = expenseTypeItem.mstrDescription
        holder.vwColorExpenseType.setBackgroundColor(Color.parseColor(expenseTypeItem.mstrColor))
        holder.ivImageViewMenu.setOnClickListener { aview ->
            val popupMenu = PopupMenu(mcontract.currentContext(), aview)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ctxmenudelete -> {
                        MessageDialog.showMessageDialog(mcontract.currentContext(),
                                mcontract.currentContext().resources.getString(R.string.msgAreYouSure),
                                DialogInterface.OnClickListener { _, _ ->
                                    val expenseTypeTarget : ExpenseType = mltExpenseTypeList[position]
                                    Observable.just(true)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe { _ ->
                                                DindinApp.mlcmDataManager?.mappDataBaseBuilder?.expenseTypeDao()?.delete(expenseTypeTarget)
                                                mltExpenseTypeList.removeAt(position)
                                                Observable.just(true)
                                                        .subscribeOn(AndroidSchedulers.mainThread())
                                                        .subscribe {
                                                            notifyItemRemoved(position)
                                                        }
                                            }
                                },
                                DialogInterface.OnClickListener { adialog, _ ->
                                    adialog.dismiss()
                                })
                        true
                    }
                    R.id.ctxmenuedit -> {
                        val intent = Intent(mcontract.currentContext(), InsertExpenseTypeActivity::class.java)
                        val nExpenseTypeId: Long? = mltExpenseTypeList[position].mnExpenseTypeID
                        intent.putExtra(InsertExpenseTypeActivity.INTENT_KEY_EXPENSETYPEID, nExpenseTypeId)
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

    class ExpenseTypeListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val vwColorExpenseType: View = avwView.findViewById(R.id.vwColorRepresentation)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val ivImageViewMenu: ImageView = avwView.findViewById(R.id.ivMenuOption)
    }
}
