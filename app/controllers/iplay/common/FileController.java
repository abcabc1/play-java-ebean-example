package controllers.iplay.common;

import com.typesafe.config.Config;
import controllers.BasicController;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.Files;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class FileController extends BasicController {

    @Inject
    public FileController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
    }

    public Result upload(Http.Request request) throws InterruptedException {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile("file");
        if (picture != null) {
            String fileName = picture.getFilename();
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            Files.TemporaryFile file = picture.getRef();
            file.copyTo(Paths.get("/Users/yb/Desktop/destination.png"), false);
            TimeUnit.SECONDS.sleep(10);
            return ok("File uploaded");
        } else {
            return badRequest().flashing("error", "Missing file");
        }
    }
}
