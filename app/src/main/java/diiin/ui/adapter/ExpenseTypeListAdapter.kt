package diiin.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.ExpenseType
import diiin.ui.activity.InsertExpenseType
import diiin.util.MessageDialog


/**
 * This adapter is the manager of expense list.
 * @author Gabriel Moro
 */
class ExpenseTypeListAdapter(actxContext : Context, alstExpenseList: ArrayList<ExpenseType>)
    : RecyclerView.Adapter<ExpenseTypeListAdapter.ExpenseTypeListItemViewHolder>() {

    val mltExpenseTypeList: ArrayList<ExpenseType> = alstExpenseList
    private val mctContext: Context = actxContext

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
            val popupMenu = PopupMenu(mctContext, aview)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.ctxmenudelete -> {
                        MessageDialog.showMessageDialog(mctContext,
                                mctContext.resources.getString(R.string.msgAreYouSure),
                                DialogInterface.OnClickListener { adialog, _ ->
                                    val expenseTypeTarget: ExpenseType = mltExpenseTypeList[position]
                                    val lstExpenses = StaticCollections.mappDataBuilder?.expenseDao()?.all()
                                    val bIsItPossibleToClear = lstExpenses?.filter { it.mnExpenseType!! == expenseTypeTarget.mnExpenseTypeID}?.count() == 0
                                    if(bIsItPossibleToClear) {
                                        StaticCollections.mappDataBuilder?.expenseTypeDao()?.delete(expenseTypeTarget)
                                        mltExpenseTypeList.removeAt(position)
                                        notifyItemRemoved(position)
                                    }
                                },
                                DialogInterface.OnClickListener { adialog, _ ->
                                    adialog.dismiss()
                                })
                        true
                    }
                    R.id.ctxmenuedit -> {
                        val intent = Intent(mctContext, InsertExpenseType::class.java)
                        val nExpenseTypeId : Long? = mltExpenseTypeList[position].mnExpenseTypeID
                        intent.putExtra(InsertExpenseType.INTENT_KEY_EXPENSETYPEID, nExpenseTypeId)
                        mctContext.startActivity(intent)
                        true
                    }
                    else -> {  false }
                }
            }
            popupMenu.show()
        }
    }

    class ExpenseTypeListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val vwColorExpenseType: View = avwView.findViewById(R.id.vwColorRepresentation)
        val tvExpenseType: TextView = avwView.findViewById(R.id.tvExpenseType)
        val ivImageViewMenu : ImageView = avwView.findViewById(R.id.ivMenuOption)
    }
}
