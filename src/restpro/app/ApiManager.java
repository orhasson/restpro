package restpro.app;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import api.AddChild;
import api.AddParent;
import api.ChildLocation;
import api.Login;

@Path("apimanager")
public class ApiManager {

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String msgFromClient) {
		System.out.println(msgFromClient);
		String messageToClient = Login.login(msgFromClient);
		switch (messageToClient) {
		case "OK":
			return Response.ok(messageToClient).build();
		case "NOT":
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to login with given credentials.").build();
        default: return Response.status(Status.BAD_REQUEST).entity("Bad request").build();
		}
	}

	// TODO : code review add if else to result
	@POST
	@Path("/addparent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addParent(String msgFromClient) {
		System.out.println(msgFromClient);
		String messageToClient = AddParent.addGivenParent(msgFromClient);
		return Response.ok(messageToClient).build();
	}

	// TODO : code review code review add if else to result
	@POST
	@Path("/addchild")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addChild(String msgFromClient) {
		System.out.println(msgFromClient);
		String messageToClient = AddChild.addGivenChild(msgFromClient);
		return Response.ok(messageToClient).build();
	}

	// TODO : code review
	@POST
	@Path("/childlocation")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChildLocation(String msgFromClient) {
		System.out.println(msgFromClient);
		String messageToClient = ChildLocation.getChildLocation(msgFromClient);
		return Response.ok(messageToClient).build();
	}
}
