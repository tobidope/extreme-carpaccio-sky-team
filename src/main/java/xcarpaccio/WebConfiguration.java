package xcarpaccio;

import net.codestory.http.Configuration;
import net.codestory.http.payload.Payload;
import net.codestory.http.routes.Routes;

public class WebConfiguration implements Configuration {

    private final Logger logger = new Logger();

    @Override
    public void configure(Routes routes) {
        routes.
                get("/ping", "pong").
                post("/feedback", (context) -> {
                    Message message = context.extract(Message.class);
                    //if (!message.type.equals("WIN")) {
                    	logger.log(message.type + ": " + message.message);
                    //}
                    return new Payload(204);
                }).
                post("/quote", (context -> {
                    String method = context.method();
                    String uri = context.uri();
                    String body = context.extract(String.class);
                    logger.log(method + " " + uri + " " + body);
                    Order order = context.extract(Order.class);
                    logger.log("Unserialized order: " + order);
                    try {
	                    Answer answer = order.createQuote();
	                    return new Payload("application/json", answer, 200);
                    } catch (Exception e) {
                    	return new Payload(400);
					} 
                }))
        ;
    }
}
