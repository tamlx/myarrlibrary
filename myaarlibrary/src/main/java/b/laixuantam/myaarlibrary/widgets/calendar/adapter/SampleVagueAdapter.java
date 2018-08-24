
package b.laixuantam.myaarlibrary.widgets.calendar.adapter;

import android.support.annotation.LayoutRes;

import b.laixuantam.myaarlibrary.R;

public class SampleVagueAdapter extends VagueAdapter {

    public SampleVagueAdapter() {
        super(R.layout.def_date_layout);
    }

    /**
     * Initialization adapter.
     *
     * @param dayLayout layout for date
     */
    public SampleVagueAdapter(@LayoutRes int dayLayout) {
        super(dayLayout);
    }

}
