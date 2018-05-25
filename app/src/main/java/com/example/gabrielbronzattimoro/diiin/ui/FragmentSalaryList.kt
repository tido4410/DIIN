package com.example.gabrielbronzattimoro.diiin.ui

class FragmentSalaryList : Fragment() {

    private var mspMonthSelector: Spinner? = null
    private var mrvSalaryList: RecyclerView? = null
    private var mbtnInsertSalary: Button? = null

    companion object {
        val TAGNAME = "FragmentSalaryList"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_salarylist, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mspMonthSelector = view?.findViewById(R.id.spMonthSelector)
        mrvSalaryList = view?.findViewById(R.id.rvSalaryList)
        mbtnInsertSalary = view?.findViewById(R.id.btnaddSalary)

    }

}