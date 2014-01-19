package de.htwg.seapal.web.controllers;

import com.google.inject.Inject;
import de.htwg.seapal.controller.*;
import de.htwg.seapal.model.*;
import de.htwg.seapal.model.impl.*;
import de.htwg.seapal.utils.logging.ILogger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class HelpAPI
        extends Controller {
    @Inject
    private IBoatController boatController;

    @Inject
    private IMarkController markController;

    @Inject
    private IAccountController AccountController;

    @Inject
    private IRouteController routeController;

    @Inject
    private ITripController tripController;

    @Inject
    private IWaypointController waypointController;

    @Inject
    private ILogger logger;

    public Result help()
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        ObjectNode node = Json.newObject();
        Map<String, JsonNode> dom = new HashMap<>();
        Map<String, JsonNode> domACL = new HashMap<>();

        IAccount crewMember1 = new Account();
        crewMember1.setAccount(crewMember1.getUUID().toString());
        crewMember1.setEmail("crewMember1@123.de");
        crewMember1.setPassword("test");
        AccountController.saveAccount(crewMember1, true);
        domACL.put("crewMember1", Json.toJson(AccountController.getPerson(crewMember1.getUUID())));

        IAccount crewMember2 = new Account();
        crewMember2.setAccount(crewMember2.getUUID().toString());
        crewMember2.setEmail("crewMember2@123.de");
        crewMember2.setPassword("test");
        AccountController.saveAccount(crewMember2, true);
        domACL.put("crewMember2", Json.toJson(AccountController.getPerson(crewMember2.getUUID())));

        IAccount account = new Account();
        account.setAccount(account.getUUID().toString());
        account.addFriend(crewMember1);
        crewMember1.addFriend(account);
        account.setEmail("account@123.de");
        account.setPassword("test");
        AccountController.saveAccount(account, true);

        domACL.put("captain", Json.toJson(AccountController.getPerson(account.getUUID())));

        ObjectNode nodeInner = Json.newObject();
        nodeInner.putAll(domACL);
        dom.put("captainAndCrew", nodeInner);

        String owner = account.getUUID().toString();

        IBoat boat = new Boat();
        boat.setBoatName("boat1");
        boat.setAccount(owner);
        boatController.saveBoat(boat);
        dom.put("boat", Json.toJson(boatController.getBoat(boat.getUUID())));

        IBoat boat2 = new Boat();
        boat2.setBoatName("boat2");
        boat2.setAccount(crewMember1.getUUID().toString());
        boatController.saveBoat(boat2);
        dom.put("boat2", Json.toJson(boatController.getBoat(boat2.getUUID())));

        IMark mark = new Mark();
        mark.setAccount(owner);
        mark.setLatitude(3.4);
        mark.setLongitude(5.6);
        markController.saveMark(mark);
        dom.put("mark", Json.toJson(markController.getMark(mark.getUUID())));

        IRoute route = new Route();
        route.setAccount(owner);
        routeController.saveRoute(route);
        dom.put("route", Json.toJson(routeController.getRoute(route.getUUID())));

        ITrip trip = new Trip();
        trip.setAccount(owner);
        trip.setBoat(boat.getUUID().toString());
        tripController.saveTrip(trip);
        dom.put("trip", Json.toJson(tripController.getTrip(trip.getUUID())));

        IWaypoint waypoint = new Waypoint();
        waypoint.setAccount(owner);
        waypoint.setTrip(trip.getUUID().toString());
        waypoint.setBoat(boat.getUUID().toString());
        waypointController.saveWaypoint(waypoint);
        dom.put("waypoint", Json.toJson(waypointController.getWaypoint(waypoint.getUUID())));

        node.putAll(dom);

        return ok(node);
    }
}
