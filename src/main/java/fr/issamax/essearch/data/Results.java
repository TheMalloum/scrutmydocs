package fr.issamax.essearch.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;

public class Results {

	protected Collection<Result> results=new ArrayList<Result>();
	protected String took;
	protected boolean rendered = false;
	protected SearchResponse searchResponse;
	
	
	public Results(SearchResponse searchResponse) {
		this.searchResponse = searchResponse;
		
		this.took = searchResponse.getTook().format();

		for (SearchHit searchHit : searchResponse.getHits()) {
			Result result = new Result(searchHit);
			searchHit.getHighlightFields();
			searchHit.getId();
			result.setTitle(searchHit.getSource().get("name").toString());

			if (searchHit.getHighlightFields() != null) {
				for (HighlightField highlightField : searchHit
						.getHighlightFields().values()) {

					String[] fragmentsBuilder = highlightField.getFragments();

					for (String fragment : fragmentsBuilder) {
						result.getFragments().add(fragment);
					}
				}
			}

			this.results.add(result);
		}
		
		this.rendered = !this.results.isEmpty();
	}

	public String getTook() {
		return took;
	}

	public void setTook(String took) {
		this.took = took;
	}

	public Collection<Result> getResults() {
		return results;
	}

	public void setResults(Collection<Result> results) {
		this.results = results;
	}
	
	public boolean isRendered() {
		return rendered;
	}
	
	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
	public SearchResponse getSearchResponse() {
		return searchResponse;
	}
	
	public void setSearchResponse(SearchResponse searchResponse) {
		this.searchResponse = searchResponse;
	}
}