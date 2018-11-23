package diiin.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import br.com.gbmoro.diiin.R
import diiin.DindinApp
import diiin.model.Salary
import diiin.ui.activity.InsertSalaryActivity
import diiin.util.MathService
import diiin.util.MessageDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SalaryListAdapterContract {
    fun currentContext() : Context
    fun onRemoveItem()
}

/**
 * This adapter is the manager of salary list.
 * @author Gabriel Moro
 */

class SalaryListAdapter(alstSalaryList: ArrayList<Salary>, acontract: SalaryListAdapterContract) : RecyclerView.Adapter<SalaryListAdapter.SalaryListItemViewHolder>() {

    val mltSalaryList: ArrayList<Salary> = alstSalaryList
    private val mcontract : SalaryListAdapterContract = acontract

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SalaryListItemViewHolder? {
        return SalaryListItemViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.icoming_item_card, parent, false))
    }

    override fun getItemCount(): Int {
        return mltSalaryList.size
    }

    override fun onBindViewHolder(holder: SalaryListItemViewHolder?, position: Int) {
        val salaryItem = mltSalaryList[position]

        if (salaryItem.msValue != null)
            holder?.tvValue?.text = MathService.formatFloatToCurrency(salaryItem.msValue!!)

        holder?.tvTitle?.text = salaryItem.mstrSource

        holder?.ivImageViewMenu?.setOnClickListener { aview ->
            val popupMenu = PopupMenu(mcontract.currentContext(), aview)
            popupMenu.inflate(R.menu.context_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.ctxmenudelete -> {
                        MessageDialog.showMessageDialog(mcontract.currentContext(),
                                mcontract.currentContext().resources.getString(R.string.msgAreYouSure),
                                DialogInterface.OnClickListener { adialog, _ ->
                                    Observable.just(true)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe {
                                                val salaryTarget = mltSalaryList[position]
                                                DindinApp.mlcmDataManager?.mappDataBaseBuilder?.salaryDao()?.delete(salaryTarget)
                                                mltSalaryList.removeAt(position)
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
                        val intent = Intent(mcontract.currentContext(), InsertSalaryActivity::class.java)
                        val nSalaryId: Long? = mltSalaryList[position].mnID
                        intent.putExtra(InsertSalaryActivity.INTENT_KEY_SALARYID, nSalaryId)
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


    class SalaryListItemViewHolder(avwView: View) : RecyclerView.ViewHolder(avwView) {
        val tvValue: TextView = avwView.findViewById(R.id.tvValue)
        val tvTitle: TextView = avwView.findViewById(R.id.tvTitle)
        val ivImageViewMenu: ImageView = avwView.findViewById(R.id.ivMenuOption)
    }
}