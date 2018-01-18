package fr.ensicaen.present.present.splash;

/**
 * Created by jueast on 03/12/17.
 */

public final class SplashPresenter implements ISplashPresenter {

    private ISplashScreenView _view;

    public SplashPresenter(ISplashScreenView view) {
        _view = view;
    }

    @Override
    public void checkTokenValidity() {
        /**@TOTO replace with real token verification*/
        _view.openLoginActivity();
        _view.finish();
    }
}
