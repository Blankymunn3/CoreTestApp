package io.bitsound.coretestapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.zcw.togglebutton.ToggleButton;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bitsound.coretestapp.R;
import io.bitsound.coretestapp.presenters.MainPresenter;
import io.bitsound.coretestapp.view.MainView;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
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
    @BindView(R.id.core_type_toggle)
    public MultiStateToggleButton coreTypeToggle;

    @BindArray(R.array.frame_types)
    public String[] frameTypeString;

    private MaterialDialog csParamDialog;
    private MaterialDialog detectParamDialog;
    private MaterialDialog unitBufferSizeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.mainPresenter = new MainPresenter();
        this.mainPresenter.setMainView(this);

        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        if (mainPresenter.isPreambleCsSelected()) {
            preambleCsToggleButton.toggleOn();
        } else {
            preambleCsToggleButton.toggleOff();
        }

        if (mainPresenter.isEnergyDetectorSelected()) {
            energyDetectorToggleButton.toggleOn();
        } else {
            energyDetectorToggleButton.toggleOff();
        }

        if (mainPresenter.isQokShapingSelected()) {
            qokShapingToggleButton.toggleOn();
        } else {
            qokShapingToggleButton.toggleOff();
        }

        if (mainPresenter.isLocalSyncFinderSelected()) {
            localSyncFinderToggleButton.toggleOn();
        } else {
            localSyncFinderToggleButton.toggleOff();
        }

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

        coreTypeToggle.setStates(new boolean[]{true, false});
        coreTypeToggle.setOnValueChangedListener(new MultiStateToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                mainPresenter.setCoreType(position);
            }
        });

    }

    @OnClick(R.id.start_button)
    public void onStartButtonClick() {
        mainPresenter.startPerformanceRecord();
    }

    @OnClick(R.id.signal_cycle)
    public void onSignalCycleButtonClick() {

    }

    @OnClick(R.id.data_cs_param_button)
    public void onDataCsParamButtonClick() {
        final View root = getLayoutInflater().inflate(R.layout.dialog_cs_param, null);
        csParamDialog = new MaterialDialog(this)
                .setView(root)
                .setPositiveButton(getString(R.string.dialog_ok_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText noSigThresholdEdittext = (EditText) root.findViewById(R.id.no_sig_threshold_edittext);
                        EditText combiningThresholdEdittext = (EditText) root.findViewById(R.id.combining_threshold_edittext);

                        String noSigStr = noSigThresholdEdittext.getText().toString();
                        String combiningStr = combiningThresholdEdittext.getText().toString();

                        int noSigThreshold;
                        int combiningThreshold;

                        if (!TextUtils.isEmpty(noSigStr)) {
                            noSigThreshold = Integer.parseInt(noSigStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.no_sig_required_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isEmpty(combiningStr)) {
                            combiningThreshold = Integer.parseInt(combiningStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.combining_required_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainPresenter.setCsParam(noSigThreshold, combiningThreshold);

                        csParamDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        csParamDialog.dismiss();
                    }
                });

        csParamDialog.show();
    }

    @OnClick(R.id.detect_param_button)
    public void onDetectParamButtonClick() {
        final View root = getLayoutInflater().inflate(R.layout.dialog_detect_param, null);
        detectParamDialog = new MaterialDialog(this)
                .setView(root)
                .setPositiveButton(getString(R.string.dialog_ok_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText recEdittext = (EditText) root.findViewById(R.id.rec_edittext);
                        EditText gammaEdittext = (EditText) root.findViewById(R.id.gamma_edittext);

                        String recStr = recEdittext.getText().toString();
                        String gammaStr = gammaEdittext.getText().toString();

                        double rec;
                        double gamma;

                        if (!TextUtils.isEmpty(recStr)) {
                            rec = Double.parseDouble(recStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.rec_required_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isEmpty(gammaStr)) {
                            gamma = Double.parseDouble(gammaStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.gamma_required_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainPresenter.setDetectParam(rec, gamma);

                        detectParamDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detectParamDialog.dismiss();
                    }
                });

        detectParamDialog.show();
    }

    @OnClick(R.id.unit_buffer_param_button)
    public void onUnitBufferParamButtonClick() {
        final View root = getLayoutInflater().inflate(R.layout.dialog_unit_buffer_size, null);
        unitBufferSizeDialog = new MaterialDialog(this)
                .setView(root)
                .setPositiveButton(getString(R.string.dialog_ok_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText unitBufferSizeEdittext = (EditText) root.findViewById(R.id.unit_buffer_size_edittext);

                        String unitBufferSizeStr = unitBufferSizeEdittext.getText().toString();

                        double unitBufferSize;

                        if (!TextUtils.isEmpty(unitBufferSizeStr)) {
                            unitBufferSize = Integer.parseInt(unitBufferSizeStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.unit_buffer_size_required_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainPresenter.setUnitBufferSize(unitBufferSize);

                        detectParamDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detectParamDialog.dismiss();
                    }
                });

        detectParamDialog.show();
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

