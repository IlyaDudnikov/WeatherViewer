package com.ilyadudnikov.weatherViewer.services;

import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {OpenWeatherApiService.class})
public class OpenWeatherApiServiceIntegrationTest {
    private OpenWeatherApiService openWeatherApiService;
    private HttpClient mockHttpClient;
    private HttpResponse mockResponse;

    private static final String LOCATIONS_WITH_NAME_MOSCOW = "[{\"name\":\"Moscow\",\"local_names\":{\"kk\":\"Мәскеу\",\"ga\":\"Moscó\",\"mt\":\"Moska\",\"no\":\"Moskva\",\"vo\":\"Moskva\",\"sr\":\"Москва\",\"tg\":\"Маскав\",\"cu\":\"Москъва\",\"sk\":\"Moskva\",\"ja\":\"モスクワ\",\"fi\":\"Moskova\",\"ku\":\"Moskow\",\"ch\":\"Moscow\",\"ka\":\"მოსკოვი\",\"mr\":\"मॉस्को\",\"yi\":\"מאסקווע\",\"ms\":\"Moscow\",\"av\":\"Москва\",\"te\":\"మాస్కో\",\"ie\":\"Moskwa\",\"he\":\"מוסקווה\",\"gd\":\"Moscobha\",\"am\":\"ሞስኮ\",\"sl\":\"Moskva\",\"bg\":\"Москва\",\"sh\":\"Moskva\",\"hi\":\"मास्को\",\"os\":\"Мæскуы\",\"fa\":\"مسکو\",\"tt\":\"Мәскәү\",\"it\":\"Mosca\",\"qu\":\"Moskwa\",\"sw\":\"Moscow\",\"cs\":\"Moskva\",\"mk\":\"Москва\",\"sc\":\"Mosca\",\"mi\":\"Mohikau\",\"kg\":\"Moskva\",\"so\":\"Moskow\",\"tl\":\"Moscow\",\"mg\":\"Moskva\",\"ky\":\"Москва\",\"br\":\"Moskov\",\"ab\":\"Москва\",\"sm\":\"Moscow\",\"de\":\"Moskau\",\"ba\":\"Мәскәү\",\"gl\":\"Moscova-Москва\",\"co\":\"Moscù\",\"jv\":\"Moskwa\",\"dv\":\"މޮސްކޯ\",\"ur\":\"ماسکو\",\"ar\":\"موسكو\",\"be\":\"Масква\",\"ia\":\"Moscova\",\"ak\":\"Moscow\",\"da\":\"Moskva\",\"ko\":\"모스크바\",\"lt\":\"Maskva\",\"zh\":\"莫斯科\",\"bn\":\"মস্কো\",\"ln\":\"Moskú\",\"ml\":\"മോസ്കോ\",\"fy\":\"Moskou\",\"mn\":\"Москва\",\"za\":\"Moscow\",\"af\":\"Moskou\",\"th\":\"มอสโก\",\"tk\":\"Moskwa\",\"ps\":\"مسکو\",\"vi\":\"Mát-xcơ-va\",\"gv\":\"Moscow\",\"kl\":\"Moskva\",\"hr\":\"Moskva\",\"na\":\"Moscow\",\"ta\":\"மாஸ்கோ\",\"se\":\"Moskva\",\"ty\":\"Moscou\",\"az\":\"Moskva\",\"oc\":\"Moscòu\",\"nl\":\"Moskou\",\"el\":\"Μόσχα\",\"iu\":\"ᒨᔅᑯ\",\"et\":\"Moskva\",\"sq\":\"Moska\",\"is\":\"Moskva\",\"id\":\"Moskwa\",\"uz\":\"Moskva\",\"kn\":\"ಮಾಸ್ಕೋ\",\"lv\":\"Maskava\",\"sg\":\"Moscow\",\"ay\":\"Mosku\",\"es\":\"Moscú\",\"cy\":\"Moscfa\",\"io\":\"Moskva\",\"ce\":\"Москох\",\"wa\":\"Moscou\",\"st\":\"Moscow\",\"kv\":\"Мӧскуа\",\"hy\":\"Մոսկվա\",\"fo\":\"Moskva\",\"kw\":\"Moskva\",\"nn\":\"Moskva\",\"hu\":\"Moszkva\",\"feature_name\":\"Moscow\",\"dz\":\"མོསི་ཀོ\",\"an\":\"Moscú\",\"ru\":\"Москва\",\"su\":\"Moskwa\",\"ascii\":\"Moscow\",\"en\":\"Moscow\",\"bo\":\"མོ་སི་ཁོ།\",\"fr\":\"Moscou\",\"sv\":\"Moskva\",\"yo\":\"Mọsko\",\"la\":\"Moscua\",\"lg\":\"Moosko\",\"zu\":\"IMoskwa\",\"ss\":\"Moscow\",\"pl\":\"Moskwa\",\"wo\":\"Mosku\",\"ht\":\"Moskou\",\"uk\":\"Москва\",\"ca\":\"Moscou\",\"cv\":\"Мускав\",\"eu\":\"Mosku\",\"ro\":\"Moscova\",\"pt\":\"Moscou\",\"my\":\"မော်စကိုမြို့\",\"eo\":\"Moskvo\",\"tr\":\"Moskova\",\"gn\":\"Mosku\",\"bi\":\"Moskow\",\"bs\":\"Moskva\",\"nb\":\"Moskva\",\"ug\":\"Moskwa\",\"li\":\"Moskou\"},\"lat\":55.7504461,\"lon\":37.6174943,\"country\":\"RU\",\"state\":\"Moscow\"},{\"name\":\"Moscow\",\"local_names\":{\"en\":\"Moscow\",\"ru\":\"Москва\"},\"lat\":46.7323875,\"lon\":-117.0001651,\"country\":\"US\",\"state\":\"Idaho\"},{\"name\":\"Moscow\",\"lat\":45.071096,\"lon\":-69.891586,\"country\":\"US\",\"state\":\"Maine\"},{\"name\":\"Moscow\",\"lat\":35.0619984,\"lon\":-89.4039612,\"country\":\"US\",\"state\":\"Tennessee\"},{\"name\":\"Moscow\",\"lat\":39.5437014,\"lon\":-79.0050273,\"country\":\"US\",\"state\":\"Maryland\"}]";
    private static final String LOCATION_WITH_MOSCOW_COORDINATES = "{\"coord\":{\"lon\":-0.1293,\"lat\":51.5108},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clearsky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":12.18,\"feels_like\":11.73,\"temp_min\":10.11,\"temp_max\":13.33,\"pressure\":1029,\"humidity\":87,\"sea_level\":1029,\"grnd_level\":1025},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":84,\"gust\":1.34},\"clouds\":{\"all\":8},\"dt\":1730314388,\"sys\":{\"type\":2,\"id\":2075535,\"country\":\"GB\",\"sunrise\":1730271057,\"sunset\":1730306232},\"timezone\":0,\"id\":2643743,\"name\":\"London\",\"cod\":200}";

    @BeforeEach
    public void setUp() {
        openWeatherApiService = new OpenWeatherApiService();
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockResponse = Mockito.mock(HttpResponse.class);

        ReflectionTestUtils.setField(openWeatherApiService, "httpClient", mockHttpClient);
    }

    @Test
    void testGetLocationsByName() throws Exception {

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(LOCATIONS_WITH_NAME_MOSCOW);

        List<LocationApiResponse> locations = openWeatherApiService.getLocationsByName("Moscow");

        assertEquals(5, locations.size());
        assertEquals("Moscow", locations.get(0).getName());
        assertEquals(new BigDecimal("55.7504461"), locations.get(0).getLatitude());
        assertEquals(new BigDecimal("37.6174943"), locations.get(0).getLongitude());

    }

    @Test
    void testGetLocationWithWeather() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(LOCATION_WITH_MOSCOW_COORDINATES);

        Location location = Location.builder()
                .name("London")
                .latitude(new BigDecimal("51.5108"))
                .longitude(new BigDecimal("-0.1293"))
                .build();
        LocationWithWeatherApiResponse locationWithWeather = openWeatherApiService.getLocationWithWeather(location);

        assertEquals("London", locationWithWeather.getName());
        assertEquals(11.73, locationWithWeather.getMain().getFeelsLike());
        assertEquals("clearsky", locationWithWeather.getWeather().get(0).getDescription());
    }
}
