package jsonprotocol;

import com.model.Employee;
import dto.DTOUtils;

public class JsonProtocolUtils {
    public static Request createAddRequest(Employee employee) {
        Request req = new Request();
        req.setType(RequestType.ADD_EMPLOYEE);
        req.setEmployee(DTOUtils.getDTO(employee));
        return req;
    }

    public static Request createUpdateRequest(Employee employee) {
        Request req = new Request();
        req.setType(RequestType.UPDATE_EMPLOYEE);
        req.setEmployee(DTOUtils.getDTO(employee));
        return req;
    }

    public static Request createDeleteRequest(Employee employee) {
        Request req = new Request();
        req.setType(RequestType.DELETE_EMPLOYEE);
        req.setEmployee(DTOUtils.getDTO(employee));
        return req;
    }

    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }
}
