package controller;

import com.jfinal.core.Controller;

/**
 * Created by liweiqi on 2014/11/10.
 */
public class MainController extends Controller {
    public void index() {
        render("html/loginIn.html");
    }

    public void main() {
        render("html/index.html");
    }

}
