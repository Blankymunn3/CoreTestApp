package io.bitsound.coretestapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bitsound.coretestapp.R;
import io.bitsound.coretestapp.presenters.MainPresenter;
import io.bitsound.coretestapp.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;

    @BindView(R.id.toggle_preamble_cs)
    public ToggleButton preambleCsToggleButton;
    @BindView(R.id.toggle_energy_detector)
    public ToggleButton energyDetectorToggleButton;
    @BindView(R.id.toggle_qok_shaping)
    public ToggleButton qokShapingToggleButton;
    @BindView(R.id.toggle_local_sync_finder)
    public ToggleButton localSyncFinderToggleButton;
    @BindView(R.id.frame_type_spinner)
    public MaterialSpinner frameTypeSpinner;

    @BindArray(R.array.frame_types)
    public String[] frameTypeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.mainPresenter = new MainPresenter();
        this.mainPresenter.setMainView(this);

        preambleCsToggleButton.setToggleOff(false);
        energyDetectorToggleButton.setToggleOff(false);
        qokShapingToggleButton.setToggleOff(false);
        localSyncFinderToggleButton.setToggleOff(false);

        preambleCsToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mainPresenter.setPreambleCsSelected(on);
            }
        });

        energyDetectorToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mainPresenter.setEnergyDetectorSelected(on);
            }
        });

        qokShapingToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mainPresenter.setQokShapingSelected(on);
            }
        });

        localSyncFinderToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                mainPresenter.setLocalSyncFinderSelected(on);
            }
        });

        frameTypeSpinner.setItems(frameTypeString);

        frameTypeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mainPresenter.setFrameType(position);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        this.mainPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mainPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mainPresenter.destroy();
    }
}

