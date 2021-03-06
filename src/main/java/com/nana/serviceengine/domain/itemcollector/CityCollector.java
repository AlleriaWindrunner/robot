package com.nana.serviceengine.domain.itemcollector;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;

import com.nana.serviceengine.common.bean.DomainKeyWord;
import com.nana.serviceengine.common.bean.UserMessage;
import com.nana.serviceengine.common.dic.DomainDic;
import com.nana.serviceengine.domain.commonapi.map.MapAPI;
import com.nana.serviceengine.domain.commonapi.map.bean.Address;
import com.nana.serviceengine.neuron.domainparam.bean.ParamItem;
import com.nana.serviceengine.neuron.itemcollector.Collector;
import com.nana.serviceengine.neuron.processor.ServiceProcessor;

public class CityCollector extends Collector<String[]> {
	private static CityCollector cc = new CityCollector();

	private CityCollector() {
	}

	public static CityCollector getInstance() {
		return cc;
	}

	/**
	 * 根据分词后的结果找到句子中的城市名
	 * @param inputs
	 * @return
	 */
	public String[] parseCity(UserMessage message) {
		List<Term> terms = message.getTerms();
		List<String> res = new ArrayList<String>();
		for (int i = 0; i < terms.size(); i++) {
			DomainKeyWord dkw = DomainDic.domainKeyWord.get(terms.get(i)
					.getRealName());
			if (dkw == null)
				continue;
			if ("address".equals(dkw.getDomain())) {
				res.add(dkw.getValue());
			}
		}
		if (res.size() != 0)
			return res.toArray(new String[] {});
		else{
			Address address =MapAPI.getInstance().getDetailInfoByGps(message.getGps());
			if(address != null && address.getCity()!= null && !"".equals(address.getCity())){
				res.add(address.getCity());
				return res.toArray(new String[] {});
			}
		}
		return null;
	}


	public String[] collectParam(ParamItem paramItem,UserMessage message,ServiceProcessor processor) {
		return parseCity(message);
	}

	@Override
	public String[] initCollectParam(UserMessage message,
			ServiceProcessor processor) {
		return collectParam(null,message,processor);
	}

	@Override
	public String[] lackCollectParam(UserMessage message,
			ServiceProcessor processor) {
		return collectParam(null,message,processor);
	}

	@Override
	public String[] finishCollectParam(ParamItem paramItem,
			UserMessage message, ServiceProcessor processor) {
		return collectParam(paramItem,message,processor);
	}
}
