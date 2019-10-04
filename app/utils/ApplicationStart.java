package utils;

import play.Environment;
import play.inject.ApplicationLifecycle;
import play.routing.Router;
import repository.iplay.account.EndpointRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// This creates an `ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {

    @Inject
    private Provider<Router> router;
    @Inject
    private EndpointRepository repository;

    // Inject the application's Environment upon start-up and register hook(s) for shut-down.
    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment, Provider<Router> router, EndpointRepository repository) {

        this.router = router;
        this.repository = repository;
        System.out.println("Application Start with " + router.get().documentation().size() + " routers");
        List<Router.RouteDocumentation> rds = router.get().documentation();
//        List<Endpoint> list = rds.stream().map(v -> {
//            Endpoint endpoint = new Endpoint();
//            endpoint.pathPattern = v.getPathPattern();
//            endpoint.httpMethod = v.getHttpMethod();
//            endpoint.controllerMethod = v.getControllerMethodInvocation().replace("(request:Request)","");
//            return endpoint;
//        }).filter(v->v.controllerMethod.split("\\.").length > 3).collect(Collectors.toList());
//        Integer modifiedCount = repository.refresh(list);
//        System.out.println("There were " + modifiedCount + " rows updated");

        // Shut-down hook
        lifecycle.addStopHook(() -> {
            return CompletableFuture.completedFuture(null);
        });
        // ...
    }
}
