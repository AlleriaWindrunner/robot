package com.nana.serviceengine.domain.itemcollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.domain.Term;

import com.nana.serviceengine.common.bean.UserMessage;
import com.nana.serviceengine.neuron.domainparam.bean.ParamItem;
import com.nana.serviceengine.neuron.itemcollector.Collector;
import com.nana.serviceengine.neuron.processor.ServiceProcessor;

public class PageTurnCollector extends Collector<Integer>{
	private static PageTurnCollector pc = new PageTurnCollector();
	//这里把切换词直接写在类中，后面可以把词存在外部文件中
	private Map<String,Integer> turnCmd = null;
	
	private PageTurnCollector(){
		turnCmd = new HashMap<String,Integer>();
		turnCmd.put("上", -1);
		turnCmd.put("下", 1);
		turnCmd.put("换", 1);
		turnCmd.put("切", 1);
	}
	
	public static PageTurnCollector getInstance(){
		return pc;
	}
	/**
	 * 获得用户输入翻页命令时，对应的页面变化
	 * @param mes
	 * @return
	 */
	public Integer getIndexChange(UserMessage mes){
		Integer res = null;
		List<Term> terms = mes.getTerms();
		for(Term term:terms){
			res = turnCmd.get(term.getRealName());
			if(res != null){
				return res;
			}
		}
		return res;
	}


	@Override
	public Integer initCollectParam(UserMessage message,
			ServiceProcessor processor) {
		return getIndexChange(message);
	}

	@Override
	public Integer lackCollectParam(UserMessage message,
			ServiceProcessor processor) {
		return getIndexChange(message);
	}

	@Override
	public Integer finishCollectParam(ParamItem paramItem, UserMessage message,
			ServiceProcessor processor) {
		return getIndexChange(message);
	}
	 
}
