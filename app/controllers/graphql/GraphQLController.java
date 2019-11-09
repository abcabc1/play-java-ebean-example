package controllers.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.resolvers.DateTimeScalar;
import graphql.resolvers.Mutation;
import graphql.resolvers.Query;
import graphql.schema.GraphQLSchema;
import graphql.types.Data;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.http.RequestUtil;

public class GraphQLController extends Controller {

    public Result index() {
        return ok("OK");
    }

    public Result query(Http.Request request) {
        Data.initBooks();
        String query = RequestUtil.getRequestString(request, "query").orElse("");
        GraphQL graphQL = GraphQL.newGraphQL(buildSchema()).build();
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .build();
        ExecutionResult result = graphQL.execute(executionInput);
        return ok(Json.toJson(result.toSpecification()));
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(), new Mutation())
                .scalars(new DateTimeScalar())
                .build()
                .makeExecutableSchema();
    }
}
