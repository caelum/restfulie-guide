package br.com.caelum.vraptor.restbucks.web;

import static br.com.caelum.vraptor.view.Results.representation;
import br.com.caelum.travelandrest.Hotel;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Routes;
import br.com.caelum.vraptor.restbucks.HotelDatabase;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.view.Status;

/**
 * Ordering system provides two services: order retrieval and insertion.
 * 
 * @author guilherme silveira
 */
@Resource
public class HotelController {

	private final Result result;
	private final Status status;
	private final HotelDatabase database;
	private final Routes routes;

	public HotelController(Result result, Status status, HotelDatabase database, Routes routes) {
		this.result = result;
		this.status = status;
		this.database = database;
		this.routes = routes;
	}

	@Get
	@Path("/hotels/{hotel.id}")
	public void get(Hotel hotel) {
		hotel = database.getOrder(hotel.getId());
		if (hotel != null) {
			Serializer serializer = result.use(representation()).from(hotel);
			serializer.include("items").include("location");
			serializer.include("payment").serialize();
		} else {
			status.notFound();
		}
	}
	
	@Post
	@Path("/hotels")
	@Consumes
	public void add(Hotel hotel) {
		database.save(hotel);
		routes.uriFor(HotelController.class).get(hotel);
		status.created(routes.getUri());
	}
	
//	@Delete
//	@Path("/hotels/{order.id}")
//	@Transition
//	public void cancel(Order order) {
//		order = database.getOrder(order.getId());
//		if(order.getStatus().equals("ready")) {
//			order.finish();
//		} else {
//			order.cancel();
//			database.delete(order);
//		}
//		status.ok();
//	}
//	
//	@Get
//	@Path("/hotels")
//	public List<Order> index() throws IOException {
//		return new ArrayList<Order>(database.all());
//	}
//
//	@Post
//	@Path("/hotels/{order.id}/pay")
//	@Consumes
//	@Transition
//	public void pay(Order order, Payment payment) {
//		order = database.getOrder(order.getId());
//		if(order.pay(payment)) {
//			result.use(xml()).from(order.getReceipt()).serialize();
//		} else {
//			status.badRequest("Invalid payment value, order costs " + order.getCost());
//		}
//	}
//
//	@Get
//	@Path("/hotels/{order.id}/checkPaymentInfo")
//	public void checkPayment(Order order) {
//		order = database.getOrder(order.getId());
//		if (order != null) {
//			result.use(xml()).from(order.getPayment()).serialize();
//		} else {
//			status.notFound();
//		}
//	}
//
//	@Put
//	@Path("/hotels/{order.id}")
//	@Transition
//	@Consumes
//	public void update(Order order) {
//		order.setStatus("unpaid");
//		database.update(order);
//		// we could status.ok() or return the representation
//		get(order); 
//	}
	
}
