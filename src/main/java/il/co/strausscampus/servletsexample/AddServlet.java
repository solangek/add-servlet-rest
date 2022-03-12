package il.co.strausscampus.servletsexample;

import il.co.strausscampus.servletsexample.exceptions.MissingOperandException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Stream;

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

        // Response headers
        response.setContentType("text/json");

        // Allowing React client side development on a different server:
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Response body
        PrintWriter writer = response.getWriter();

        try {
            validateParameters(request.getParameterMap().keySet());

            int leftOperand = Integer.parseInt(request.getParameter("left"));
            int rightOperand = Integer.parseInt(request.getParameter("right"));

            System.out.printf("Received params: %d, %d%n", leftOperand, rightOperand);

            // If we got here with no exception, then everything is okay, and we can
            // fulfill the request.
            response.setStatus(HttpServletResponse.SC_OK);

            // Here we write JSON manually just to simplify the example,
            // usually we will use libraries to do that to avoid syntax error.
            int result = leftOperand + rightOperand;
            writer.printf("{\"result\":%d}%n", result);

            System.out.printf("Sent result %d%n", result);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.println("{\"error\":\"operands must be integers\"}");
        } catch (MissingOperandException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.printf("{\"error\":%s}", e.getMessage());
        } finally {
            writer.flush();
            writer.close();
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
        for (String name : receivedParamNames) {
            if (!(name.equals(PARAM_LEFT) || name.equals(PARAM_RIGHT))) {
                throw new MissingOperandException(PARAM_LEFT, PARAM_RIGHT);
            }
        }

        // Alternative syntax:
//        if (!Stream.of(PARAM_LEFT, PARAM_RIGHT).allMatch(receivedParamNames::contains)) {
//            throw new MissingOperandException(PARAM_LEFT, PARAM_RIGHT);
//        }
    }
}
