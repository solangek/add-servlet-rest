package hac;

import hac.exceptions.MissingOperandException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Set;

/**
 * This API endpoint (an endpoint is a function exposed by an API) receives two numbers and returns their sum.
 */
@WebServlet(name = "AddServlet", value = "/add")
public class AddServlet extends HttpServlet {

    /**
     * The parameter name of the left operand,
     */
    private static final String PARAM_LEFT = "left";
    /**
     * The parameter name of the right operand.
     */
    private static final String PARAM_RIGHT = "right";

    /**
     * Handles a GET request for this servlet - receives two integers and returns their sum.
     * <br/>
     * @param request data of the incoming request from the client
     * @param response provides tools to handle the compilation of the response and sending it to the client
     * @throws IOException may be thrown if sending data to the client fails for some reason.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // = null;
        // Response headers
        response.setContentType("text/json");

        // Allowing React client side development on a different server:
        response.setHeader("Access-Control-Allow-Origin", "*");


        try (JsonWriter jsonw = Json.createWriter(response.getOutputStream())) {

            try {
                validateParameters(request.getParameterMap().keySet());

                int leftOperand = Integer.parseInt(request.getParameter("left"));
                int rightOperand = Integer.parseInt(request.getParameter("right"));

                System.out.printf("Received params: %d, %d%n", leftOperand, rightOperand);

                // If we got here with no exception, then everything is okay, and we can
                // fulfill the request.
                response.setStatus(HttpServletResponse.SC_OK);

                // we should not write json strings manually, instead we should use the
                // JsonObjectBuilder class.
                int result = leftOperand + rightOperand;
                jsonw.writeObject(buildJsonResponse("result", Integer.toString(result)));
                //System.out.printf("Sent result %d%n", result);

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonw.writeObject(buildJsonResponse("error", "operands must be integers"));

            } catch (MissingOperandException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonw.writeObject(buildJsonResponse("error", e.getMessage()));
            }
        }
    }

    /**
     * Validates that all required parameters are received. If any additional parameters are received,
     * they do not matter, and we will ignore them.
     * If validation passes the function will finish with no effect. If validation fails, an exception will
     * be thrown.
     * <br/>
     * @param receivedParamNames the names of the received parameters
     * @throws MissingOperandException if any of the required parameters is missing
     */
    private void validateParameters(Set<String> receivedParamNames) throws MissingOperandException {
        if (!receivedParamNames.contains(PARAM_LEFT) || !receivedParamNames.contains(PARAM_RIGHT)) {
            throw new MissingOperandException(PARAM_LEFT, PARAM_RIGHT);
        }

        // Alternative syntax:
        //if (!Stream.of(PARAM_LEFT, PARAM_RIGHT).allMatch(receivedParamNames::contains)) {
        //    throw new MissingOperandException(PARAM_LEFT, PARAM_RIGHT);
        //}
    }

    private JsonObject buildJsonResponse(String key, Object value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(key, value.toString());
        return builder.build();
    }
}


