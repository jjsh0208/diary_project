package org.example.diary.youtube;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class YoutubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Cacheable("youtubeSearchCache")
    public HashMap<String, String> searchVideo(String query) throws IOException {
        JsonFactory jsonFactory = new JacksonFactory();
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();

        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));
        search.setKey(apiKey);
        search.setQ(query);
        search.setVideoDuration("short");
        search.setMaxResults(3L);
        search.setType(Collections.singletonList("video"));
        search.setVideoCategoryId("10");

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();

        // 3개 반환 위해...
        HashMap<String, String> youtubeInfo = new HashMap<String, String>();

        if (searchResultList != null && !searchResultList.isEmpty()) {
            for (SearchResult searchResult : searchResultList) {
                String videoId = searchResult.getId().getVideoId();
                String videoTitle = searchResult.getSnippet().getTitle();
                if(videoId == null)
                    continue;
                // 반환에 넣기
                youtubeInfo.put(videoTitle, videoId);
            }
            return youtubeInfo;
        }
        return null;
    }

}
