package io.bitsound.coretestapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bitsound.coretestapp.R;
import io.bitsound.coretestapp.presenters.MainPresenter;
import io.bitsound.coretestapp.view.MainView;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity implements MainView {

    private final int CODE_REQUEST_RECORD_AUDIO = 3;

    private MainPresenter mainPresenter;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.toggle_preamble_cs)
    public Switch preambleCsToggleButton;
    @BindView(R.id.toggle_energy_detector)
    public Switch energyDetectorToggleButton;
    @BindView(R.id.toggle_qok_shaping)
    public Switch qokShapingToggleButton;
    @BindView(R.id.toggle_local_sync_finder)
    public Switch localSyncFinderToggleButton;
    @BindView(R.id.frame_type_spinner)
    public MaterialSpinner frameTypeSpinner;
    @BindView(R.id.core_type_toggle)
    public MultiStateToggleButton coreTypeToggle;

    @BindArray(R.array.frame_types)
    public String[] frameTypeString;

    private MaterialDialog csParamDialog;
    private MaterialDialog detectParamDialog;
    private MaterialDialog unitBufferSizeDialog;
    private MaterialDialog recCountDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);

        checkPermission();

        this.mainPresenter = new MainPresenter();
        this.mainPresenter.setMainView(this);

        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        if (mainPresenter.isPreambleCsSelected()) {
            preambleCsToggleButton.setChecked(true);
        } else {
            preambleCsToggleButton.setChecked(false);
        }

        if (mainPresenter.isEnergyDetectorSelected()) {
            energyDetectorToggleButton.setChecked(true);
        } else {
            energyDetectorToggleButton.setChecked(false);
        }

        if (mainPresenter.isQokShapingSelected()) {
            qokShapingToggleButton.setChecked(true);
        } else {
            qokShapingToggleButton.setChecked(false);
        }

        if (mainPresenter.isLocalSyncFinderSelected()) {
            localSyncFinderToggleButton.setChecked(true);
        } else {
            localSyncFinderToggleButton.setChecked(false);
        }

        frameTypeSpinner.setItems(frameTypeString);

        frameTypeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mainPresenter.setFrameType(position);
            }
        });

        if (mainPresenter.getCoreType() == 0) {
            // pullpkt
            coreTypeToggle.setStates(new boolean[]{true, false});
        } else {
            // parallel
            coreTypeToggle.setStates(new boolean[]{false, true});
        }

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
        final View root = getLayoutInflater().inflate(R.layout.dialog_rec_count, null);
        recCountDialog = new MaterialDialog(this)
                .setView(root)
                .setPositiveButton(getString(R.string.dialog_ok_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText recCountEdittext = (EditText) root.findViewById(R.id.rec_count_edittext);

                        String recCountStr = recCountEdittext.getText().toString();

                        int recCount;

                        if (!TextUtils.isEmpty(recCountStr)) {
                            recCount = Integer.parseInt(recCountStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.mic_count_required_msg),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainPresenter.setRecCount(recCount);
                        recCountDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recCountDialog.dismiss();
                    }
                });

        recCountDialog.show();
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
                            Toast.makeText(MainActivity.this, getString(R.string.rec_required_msg),
                                    Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, getString(R.string.rec_required_msg),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isEmpty(gammaStr)) {
                            gamma = Double.parseDouble(gammaStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.gamma_required_msg),
                                    Toast.LENGTH_SHORT).show();
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

                        int unitBufferSize;

                        if (!TextUtils.isEmpty(unitBufferSizeStr)) {
                            unitBufferSize = Integer.parseInt(unitBufferSizeStr);
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.unit_buffer_size_required_msg),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mainPresenter.setUnitBufferSize(unitBufferSize);

                        unitBufferSizeDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        unitBufferSizeDialog.dismiss();
                    }
                });

        unitBufferSizeDialog.show();
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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permReqList = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permReqList.add(Manifest.permission.RECORD_AUDIO);
            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permReqList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permReqList.size() > 0) {
                String[] permArray = new String[permReqList.size()];
                ActivityCompat.requestPermissions(MainActivity.this,
                        permReqList.toArray(permArray), CODE_REQUEST_RECORD_AUDIO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean isRecPerm = false;
        boolean isWritePerm = false;

        for (String perm : permissions) {
            if (Manifest.permission.RECORD_AUDIO.equals(perm)
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                isRecPerm = true;
            }
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(perm)
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                isWritePerm = true;
            }
        }

        if (!isRecPerm) {
            Toast.makeText(MainActivity.this, getString(R.string.mic_permission_denied),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.mic_permission_allowed),
                    Toast.LENGTH_SHORT).show();
        }

        if (!isWritePerm) {
            Toast.makeText(MainActivity.this, getString(R.string.write_external_storage_permission_denied),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.write_external_storage_permission_allowed),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void startPerformanceRecResultActivity(boolean preambleCsSelected,
                                                  boolean energyDetectorSelected, boolean qokShapingSelected,
                                                  boolean localSyncFinderSelected, int frameType, int coreType, int noSigThreshold,
                                                  int combiningThreshold, double rec, double gamma, int unitBufferSize, int recCount) {

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);

        intent.putExtra(ResultActivity.EXTRA_PREAMBLE_CS, preambleCsSelected);
        intent.putExtra(ResultActivity.EXTRA_ENERGY_DETECTOR, energyDetectorSelected);
        intent.putExtra(ResultActivity.EXTRA_QOK_SHAPING, qokShapingSelected);
        intent.putExtra(ResultActivity.EXTRA_LOCAL_SYNC_FINDER, localSyncFinderSelected);
        intent.putExtra(ResultActivity.EXTRA_FRAME_TYPE, frameType);
        intent.putExtra(ResultActivity.EXTRA_CORE_TYPE, coreType);
        intent.putExtra(ResultActivity.EXTRA_NO_SIG_THRESHOLD, noSigThreshold);
        intent.putExtra(ResultActivity.EXTRA_COMBINING_THRESHOLD, combiningThreshold);
        intent.putExtra(ResultActivity.EXTRA_REC, rec);
        intent.putExtra(ResultActivity.EXTRA_GAMMA, gamma);
        intent.putExtra(ResultActivity.EXTRA_UNIT_BUFFER_SIZE, unitBufferSize);
        intent.putExtra(ResultActivity.EXTRA_REC_COUNT, recCount);

        startActivity(intent);
    }
}

