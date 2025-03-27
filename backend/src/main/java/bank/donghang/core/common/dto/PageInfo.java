package bank.donghang.core.common.dto;

import java.util.List;

public record PageInfo<T>(
	String pageToken,
	List<T> data,
	Boolean hasNext
) {
	public static <T> PageInfo<T> of(
		String pageToken,
		List<T> data,
		Boolean hasNext
	) {
		return new PageInfo(
			pageToken,
			data,
			hasNext
		);
	}

	public String getPageToken() {
		return pageToken;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public List<T> getData() {
		return data;
	}
}
