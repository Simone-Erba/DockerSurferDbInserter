package querying;
import com.google.common.base.Preconditions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.LoggerHandler;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Vertex extends AbstractVerticle {
  public static void main(final String[] args) {
    Vertx.vertx().deployVerticle(Vertex.class.getName());
  }
  
  @Override
  public void start() {
   final HttpServer server = this.vertx.createHttpServer();
    HttpClientOptions _httpClientOptions = new HttpClientOptions();
    final Procedure1<HttpClientOptions> _function = (HttpClientOptions it) -> {
      it.setLogActivity(true);
      it.setSsl(true);
      it.setDefaultPort(443);
    };
    HttpClientOptions _doubleArrow = ObjectExtensions.<HttpClientOptions>operator_doubleArrow(_httpClientOptions, _function);
    final HttpClient client = this.vertx.createHttpClient(_doubleArrow);
    Router _router = Router.router(this.vertx);
    final Procedure1<Router> _function_1 = (Router it) -> {
      Route _route = it.route();
      _route.handler(LoggerHandler.create());
      
      final Handler<RoutingContext> _function_2 = (RoutingContext ctx) -> {
       // final String group = ctx.request().getParam("group");
        //final String image = ctx.request().getParam("image");
    	  
        String group="library";
        String image="node";
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("/token?service=registry.docker.io&scope=repository:");
        _builder.append(group);
        _builder.append("/");
        _builder.append(image);
        _builder.append(":pull");
       
        final Handler<HttpClientResponse> _function_4 = (HttpClientResponse it_1) -> {
          final Handler<Buffer> _function_5 = (Buffer buf) -> {
            HttpServerResponse _response = ctx.response();
            final Procedure1<HttpServerResponse> _function_6 = (HttpServerResponse answer) -> {
              answer.putHeader("content-type", "application/json");
              final String token = buf.toJsonObject().getString("token");
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("/v2/");
              _builder_1.append(group);
              _builder_1.append("/");
              _builder_1.append(image);
              _builder_1.append("/tags/list");
              final Handler<HttpClientResponse> _function_7 = (HttpClientResponse it_2) -> {
                final Handler<Buffer> _function_8 = (Buffer tagsBuf) -> {
                  final JsonObject tagList = tagsBuf.toJsonObject();
                  JsonArray _jsonArray = new JsonArray();
                  System.out.println("a");
                  InputOutput.<String>println(IterableExtensions.join(tagList.getJsonArray("tags", _jsonArray), "; "));
                  answer.end(tagList.encodePrettily());
                };
                it_2.bodyHandler(_function_8);
              };
              HttpClientRequest _get = client.get("registry-1.docker.io", _builder_1.toString(), _function_7);
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("Bearer ");
              _builder_2.append(token);
              _get.putHeader("Authorization", _builder_2).end();
            };
            ObjectExtensions.<HttpServerResponse>operator_doubleArrow(_response, _function_6);
          };
          it_1.bodyHandler(_function_5);
        };
        client.getNow(
          "auth.docker.io", _builder.toString(), _function_4);
      };
      System.out.println("ciao");
      it.route(HttpMethod.GET, "/:group/:image").handler(_function_2);
      System.out.println("ciao");
    };

    final Router router = ObjectExtensions.<Router>operator_doubleArrow(_router, _function_1);
    final Handler<HttpServerRequest> _function_2 = (HttpServerRequest it) -> {
      router.accept(it);
    };

   server.requestHandler(_function_2).listen(8080);

  }
}
