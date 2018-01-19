package fr.ensicaen.present.present.presenter.launchcall;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fr.ensicaen.present.present.models.ApiResponseModel;
import fr.ensicaen.present.present.models.CallModel;
import fr.ensicaen.present.present.services.ICallService;
import fr.ensicaen.present.present.utils.Config;
import fr.ensicaen.present.present.utils.api.ServiceFactory;
import fr.ensicaen.present.present.view.launchcall.ILaunchCallView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pierr on 31/12/2017.
 */

public class LaunchCallPresenter implements ILaunchCallPresenter {

    private final ILaunchCallView _view;
    private Config _config;
    private CallModel _call;

    @Override
    public void createCall() {
        _call = null;

        ICallService service = ServiceFactory
                .createRetrofitService(ICallService.class, _config.property("API_URL"));
        ArrayList<String> _groups = new ArrayList<>();
        _groups.add("INFO_TP1");
        _groups.add("INFO_TP2");
        service.createCall(createCallPayload("007", 130, _groups))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::onVerificationComplete)
                .subscribe(this::handleLoginSuccessResponse, this::handleLoginErrorResponse);
    }

    @Override
    public void onLaunchCallButtonClick(String duration) {

        createCall();

    }

    public LaunchCallPresenter(ILaunchCallView view) {
        _view = view;

        try {
            _config = _view.getConfigAccessor();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LaunchCallPresenter(ILaunchCallView view, Config c) {
        _view = view;
        _config = c;
    }


    public void onVerificationComplete() {

        if (!isCallCreated()) {
            _view.showToast("Erreur lors de la création", Toast.LENGTH_SHORT);
        } else {
            _view.setSuccessMessage(_call.getCode());
        }
    }

    public boolean isCallCreated() {
        return _call != null;
    }


    private void handleLoginSuccessResponse(ApiResponseModel<CallModel> response) {
        _call = response.getData();
    }

    private void handleLoginErrorResponse(Throwable throwable) {
        _view.showToast("Erreur " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT);
    }


    private JSONObject createCallPayload(String _id, int _duration, ArrayList<String> _groups) {
        JSONObject jsonPayload = new JSONObject();
        JSONObject jsonData = new JSONObject();
        try {
            jsonPayload.put("id", _id);
            jsonPayload.put("duration", _duration);
            jsonPayload.put("groups", _groups.toString());

            jsonData.put("data", jsonPayload);
            return jsonData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPayload;
    }


    /* for the tests */
    public void setCall(CallModel call) {
        _call = call;
    }

    public String getCode() {
        return _call.getCode();
    }


}
