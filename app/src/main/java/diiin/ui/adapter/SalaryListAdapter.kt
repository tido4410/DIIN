package diiin.ui.adapter

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.StaticCollections
import diiin.model.Salary
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

        holder?.tvDate?.text = salaryItem.mstrDate

        if(salaryItem.msValue!=null)
            holder?.tvValue?.text = MathService.formatFloatToCurrency(salaryItem.msValue!!)

        holder?.tvSource?.text = salaryItem.mstrSource

        holder?.tvDate?.visibility = TextView.GONE

        if(mctContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            holder?.tvDate?.visibility = TextView.VISIBLE

            if (StaticCollections.mbEditMode) holder?.ivReorder?.visibility = ImageView.VISIBLE
            else holder?.ivReorder?.visibility = ImageView.GONE
        }


    class SalaryListItemViewHolder(avwView : View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvSource: TextView = avwView.findViewById(R.id.tvSource)
        val tvDate: TextView = avwView.findViewById(R.id.tvDate)
        val ivReorder : ImageView = avwView.findViewById(R.id.ivReorder)
    }
}