package diiin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.model.Salary
import diiin.ui.ActivityDeleteCellsFromList
import diiin.util.MathService

/**
 * This adapter is the manager of salary list.
 * @author Gabriel Moro
 */
class SalaryListAdapter(alstSalaryList: ArrayList<Salary>, atContext : Context)  : RecyclerView.Adapter<SalaryListAdapter.SalaryListItemViewHolder>() {

            val mltSalaryList: ArrayList<Salary> = alstSalaryList
    private val mctContext : Context = atContext

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SalaryListItemViewHolder? {
        return SalaryListItemViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.fragment_salarylist_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mltSalaryList.size
    }

    override fun onBindViewHolder(holder: SalaryListItemViewHolder?, position: Int) {
        val salaryItem = mltSalaryList[position]

        if(salaryItem.mdtDate!=null)
            holder?.tvDate?.text = MathService.calendarTimeToString(salaryItem.mdtDate!!)
        if(salaryItem.msValue!=null)
            holder?.tvValue?.text = MathService.formatFloatToCurrency(salaryItem.msValue!!)
        holder?.tvSource?.text = salaryItem.mstSource
        holder?.itemView?.setOnLongClickListener {
            if(holder.ivCheckedImage.visibility == ImageView.VISIBLE) {
                salaryItem.mbSelected = false
                holder.ivCheckedImage.visibility = ImageView.GONE
                (mctContext as ActivityDeleteCellsFromList).hideMenu()
            } else {
                salaryItem.mbSelected = true
                holder.ivCheckedImage.visibility = ImageView.VISIBLE
                (mctContext as ActivityDeleteCellsFromList).showMenu()
            }
            true
        }
    }


    class SalaryListItemViewHolder(avwView : View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvSource: TextView = avwView.findViewById(R.id.tvSource)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
        val ivCheckedImage : ImageView = avwView.findViewById(R.id.ivChecked)
    }
}