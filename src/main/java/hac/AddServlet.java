package hac;

import com.google.gson.Gson;
import hac.exceptions.MissingOperandException;
import org.ietf.jgss.GSSContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;
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

        response.setContentType("application/json");

        // Allowing React client side development on a different server:
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            validateParameters(request.getParameterMap());

            int leftOperand = Integer.parseInt(request.getParameter(PARAM_LEFT));
            int rightOperand = Integer.parseInt(request.getParameter(PARAM_RIGHT));

            // add cookie to response just for demo
            Cookie cookie = new Cookie("test-cookie", "cookie-value");
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
            // allow javascript to read the cookie
            cookie.setHttpOnly(false);
            // set path to / to allow access from any path
            cookie.setPath("/");
            response.addCookie(cookie);

            System.out.printf("Received params: %d, %d%n", leftOperand, rightOperand);

            // If we got here with no exception, then everything is okay, and we can
            // fulfill the request.
            response.setStatus(HttpServletResponse.SC_OK);
            int result = leftOperand + rightOperand;

            // convert result to json and send it to the client
            Gson gson = new Gson();
            String json = gson.toJson(new Result(result));
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("operands must be integers");

        } catch (MissingOperandException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }

    private void validateParameters(Map<String, String[]> parameterMap) throws MissingOperandException {
        if (!parameterMap.containsKey(PARAM_LEFT) || !parameterMap.containsKey(PARAM_RIGHT)) {
            throw new MissingOperandException(PARAM_LEFT, PARAM_RIGHT);
        }
    }


    // doPost does doGet - the client implements both
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
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

//    private JsonObject buildJsonResponse(String key, Object value) {
//        JsonObjectBuilder builder = Json.createObjectBuilder();
//        builder.add(key, value.toString());
//        return builder.build();
//    }
    class Result {
        int result;
        public Result(int result) {
            this.result = result;
        }
    }
}



