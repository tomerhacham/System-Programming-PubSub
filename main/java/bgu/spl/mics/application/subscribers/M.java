package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	//Fields:
	private Integer id;

	//Constructor:
	public M(int id) {
		super("M");
		this.id=id;
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		
	}

}
