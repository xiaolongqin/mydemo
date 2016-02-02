package controller;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import model.FileDesc;
import service.FileDescSrv;
import util.JsonHelp;

/**
 * Created by Tyfunwang on 2015/5/4.
 */
@ClearInterceptor(ClearLayer.ALL)
public class FileDescController extends Controller {
    public FileDescSrv fileDescSrv = FileDescSrv.getInstance();

    public void modifyDesc() {
        String filename = getPara(FileDesc.FILENAME);
        String desc = getPara(FileDesc.DESC);

        try {
            if (fileDescSrv.modifyDesc(filename, desc)) {
                renderJson(JsonHelp.buildSuccess());
                return;
            }
            renderJson(JsonHelp.buildFailed());
        } catch (Exception e) {
            renderJson(JsonHelp.buildFailed(e.getMessage()));
        }
    }
}
