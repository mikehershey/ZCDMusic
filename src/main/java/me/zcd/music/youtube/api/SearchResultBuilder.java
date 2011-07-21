package me.zcd.music.youtube.api;

import me.zcd.music.utils.StringUtils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchResultBuilder {

	public static SearchResult buildSearchResultsFromEntryNode(Node node) {
		NodeList nodes = node.getChildNodes();
		String videoIdFromId = null;
		String videoIdFromContent = null;
		String videoIdFromLinkAlternate = null;
		String title = null;
		boolean allowEmbed = true;
		boolean allowAutoPlay = true;
		boolean allowSyndicate = true;
		double rating = 0;
		String uploader = null;
		String description = null;
		int views = 0;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node childNode = nodes.item(i);
			if (childNode.getNodeName().equals("id")) {
				try {
					String text = childNode.getTextContent();
					videoIdFromId = text.substring(text.lastIndexOf(":") + 1);
				} catch (Exception e) {
				}
			} else if (childNode.getNodeName().equals("content")) {
				try {
					String text = childNode.getAttributes().getNamedItem("src").getNodeValue();
					int start = text.indexOf("/v/") + 3;
					int end = text.indexOf("?");
					videoIdFromContent = text.substring(start, end);
				} catch (Exception e) {
				}
			} else if (childNode.getNodeName().equals("link") && childNode.getAttributes().getNamedItem("rel").getNodeValue().equals("alternate")) {
				try {
					String text = childNode.getAttributes().getNamedItem("href").getNodeValue();
					int start = text.indexOf("=") + 1;
					int end = text.indexOf("&");
					videoIdFromLinkAlternate = text.substring(start, end);
				} catch (Exception e) {
				}
			} else if (childNode.getNodeName().equals("title")) {
				title = childNode.getTextContent();
			} else if (childNode.getNodeName().equals("yt:accessControl")) {
				String action = childNode.getAttributes().getNamedItem("action").getNodeValue();
				if(action.equals("embed")) {
					String permission = childNode.getAttributes().getNamedItem("permission").getNodeValue();
					allowEmbed = permission.equals("allowed");
				}
				else if(action.equals("autoPlay")) {
					String permission = childNode.getAttributes().getNamedItem("permission").getNodeValue();
					allowAutoPlay = permission.equals("allowed");
				}
				else if(action.equals("syndicate")) {
					String permission = childNode.getAttributes().getNamedItem("permission").getNodeValue();
					allowSyndicate = permission.equals("allowed");
				}
			} else if(childNode.getNodeName().equals("gd:rating")) {
				try {
					String average = childNode.getAttributes().getNamedItem("average").getNodeValue();
					rating = Double.parseDouble(average);
				} catch (Exception e) {
				}
			} else if(childNode.getNodeName().equals("yt:statistics")) {
				try {
					String viewCount = childNode.getAttributes().getNamedItem("viewCount").getNodeValue();
					views = Integer.parseInt(viewCount);
				} catch (Exception e) {
				}
			} else if(childNode.getNodeName().equals("author")) {
				try {
					NodeList childNodes = childNode.getChildNodes();
					for(int j = 0; j < childNodes.getLength(); j++) {
						Node grandchildNode = childNodes.item(j);
						if(grandchildNode.getNodeName().equals("name")) {
							uploader = grandchildNode.getTextContent();
						}
					}
				} catch (Exception e) {
				}
			} else if(childNode.getNodeName().equals("media:group")) {
				try {
					NodeList childNodes = childNode.getChildNodes();
					for(int j = 0; j < childNodes.getLength(); j++) {
						Node grandchildNode = childNodes.item(j);
						if(grandchildNode.getNodeName().equals("media:description")) {
							description = grandchildNode.getTextContent();
						}
					}
				} catch (Exception e) {
				}
			}
		}
		// build the result
		SearchResult result = new SearchResult();
		// since google seems to randomly change the schema grab 3 ids, pick the
		// most frequent one hope its right.
		result.setVideoId(StringUtils.mostFrequentString(videoIdFromId, videoIdFromContent, videoIdFromLinkAlternate));
		result.setTitle(title);
		result.setAllowAutoPlay(allowAutoPlay);
		result.setAllowEmbed(allowEmbed);
		result.setAllowSyndicate(allowSyndicate);
		result.setAverageRating(rating);
		result.setViews(views);
		result.setUploader(uploader);
		result.setDescription(description);
		return result;
	}

}
