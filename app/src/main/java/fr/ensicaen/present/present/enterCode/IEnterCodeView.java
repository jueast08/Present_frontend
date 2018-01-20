package fr.ensicaen.present.present.enterCode;


import java.io.IOException;

import fr.ensicaen.present.present.utils.Config;

/**
 * Created by Jeanne on 14/12/2017.
 */

interface IEnterCodeView {

    void displaySuccessMessage();

    void displayErrorMessage();

    Config getConfigAccessor() throws IOException;
}
