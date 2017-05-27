package org.citydb.plugins.ade_manager.transformation.graph;


import org.citydb.api.controller.LogController;
import org.citydb.api.registry.ObjectRegistry;

import agg.xt_basis.GraTra;
import agg.xt_basis.GraTraEvent;
import agg.xt_basis.GraTraEventListener;
import agg.xt_basis.GraTraOptions;
import agg.xt_basis.LayeredGraTraImpl;
import agg.xt_basis.Match;
import agg.xt_basis.Rule;

public class GraphTransformationEventListener implements GraTraEventListener {
	
	private int steps;
	private GraTra graTra;
	private final LogController Logger;

	public GraphTransformationEventListener(GraTra graTra){
		Logger = ObjectRegistry.getInstance().getLogController();
		this.graTra = graTra;
		steps = 0;
	}
	
	@Override
	public void graTraEventOccurred(GraTraEvent g) {
		
		String ruleName = "";
		
		if (g.getMessage() == GraTraEvent.MATCH_VALID) {
			Match currentMatch = g.getMatch();
			String currentMatchName = currentMatch.getName();
			Logger.debug("[" + g.getMessage() + "] Match <" + currentMatchName + "> is valid !" );
						
		} else if (g.getMessage() == GraTraEvent.STEP_COMPLETED) {
			this.steps++;
			Match currentMatch = g.getMatch();
			Rule currentRule = currentMatch.getRule();
			String currentRuleName = currentRule.getName();			
			Logger.debug("[" + g.getMessage() + "] Rule <" + currentRuleName + "> is applied ! Step" + this.steps );
			
		} else if (g.getMessage() == GraTraEvent.LAYER_FINISHED) {
			if (graTra instanceof LayeredGraTraImpl){
				if (((LayeredGraTraImpl) graTra).getCurrentLayer() >= 0) {
								
					Logger.debug("[" + g.getMessage() + "] Layer <" + ((LayeredGraTraImpl) graTra).getCurrentLayer()  + "> finished !" );
				} else if (((LayeredGraTraImpl) graTra).getGraTraOptions().hasOption(GraTraOptions.LOOP_OVER_LAYER)) {
					Logger.debug("[" + g.getMessage() + "] Loop over layer. First layer will start. !" );
				}
			}			
		} else if (g.getMessage() == GraTraEvent.TRANSFORM_FINISHED) {
			graTra.stop();									
			Logger.debug("[" + g.getMessage() + "] Transform finished !" );
		
		}			
		else if ((g.getMessage() == GraTraEvent.INPUT_PARAMETER_NOT_SET)) {			
			String s = "<" + g.getMatch().getRule().getName() + "> parameter not set!";
			Logger.error("[" + g.getMessage() + "] " + s);			
			
		} else if (g.getMessage() == GraTraEvent.NOT_READY_TO_TRANSFORM) {
			ruleName = g.getMessageText();
			String s = " Please check variables of the rule:  " + ruleName; 			
			Logger.error("[" + g.getMessage() + "] " + s);		
			
		} else if ((g.getMessage() == GraTraEvent.ATTR_TYPE_FAILED)
				|| (g.getMessage() == GraTraEvent.RULE_FAILED)
				|| (g.getMessage() == GraTraEvent.ATOMIC_GC_FAILED)
				|| (g.getMessage() == GraTraEvent.GRAPH_FAILED)) {
			String s = g.getMessageText();
			Logger.error("[" + g.getMessage() + "] " + s);
			
		} else if (g.getMessage() == GraTraEvent.NEW_MATCH) {
			Match currentMatch = g.getMatch();
			Rule currentRule = currentMatch.getRule();
			String currentRuleName = currentRule.getName();
			Logger.debug("[" + g.getMessage() + "] new match of <" + currentRuleName + "> created !" );
			
		} else if (g.getMessage() == GraTraEvent.NO_COMPLETION) {
			Match currentMatch = g.getMatch();
			Rule currentRule = currentMatch.getRule();
			String currentRuleName = currentRule.getName();
		//	Logger.debug("[" + g.getMessage() + "] no completion of current match of Rule <" + currentRuleName + "> !" );
			
		} else if (g.getMessage() == GraTraEvent.INCONSISTENT) {
			// ruleName = currentRule.getName();
			String msg = "Graph inconsistency after applying rule <"+ruleName+"> !";
			Logger.error("[" + g.getMessage() + "] " + msg);
		}		
	}

}
