package com.rhombus.api.examples;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rhombus.ApiClient;
import com.rhombus.sdk.CameraWebserviceApi;
import com.rhombus.sdk.FaceWebserviceApi;
import com.rhombus.sdk.UploadWebserviceApi;
import com.rhombus.sdk.domain.*;
import org.apache.commons.cli.*;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.ClientBuilder;
import java.util.*;
import java.util.ArrayList;
import java.sql.Timestamp;

//This script uploads a batch of faces for facial recognition preprocessing
class UploadFacesBatch
{
    private static ApiClient _apiClient;
    private static UploadWebserviceApi _uploadWebService;
    private static FaceWebserviceApi _faceWebService;
    private static Timestamp _startTime;

    public static void main(String[] args) throws Exception
    {
        _startTime = new Timestamp(System.currentTimeMillis());

        final Options options = new Options();
        options.addRequiredOption("a", "apikey", true, "Get this from your Console.");
        options.addRequiredOption("addr", "address", true, "Local Address of photo directory (photos must follow format first-last_x.jpg) or csv with comma separated photo info (addr,name)");
        options.addOption("t","time",false,"Include Timestamp in Report Title");
        options.addOption("to","timeout",true,"Number of Seconds before Timeout");

        final CommandLine commandLine;
        try
        {
            commandLine = new DefaultParser().parse(options, args);
        }
        catch (ParseException e)
        {
            System.err.println(e.getMessage());

            new HelpFormatter().printHelp(
                    "java -cp rhombus-api-examples-all.jar com.rhombus.api.examples.CheckCameraStatus",
                    options);
            return;
        }
        int timeout;
        if (commandLine.hasOption("to"))
        {
            timeout = Integer.parseInt(commandLine.getOptionValue("to"));
        }
        else
        {
            timeout = 500;
        }
        boolean includeTime = commandLine.hasOption("t");
        String addr = commandLine.getOptionValue("addr");
        String apikey = commandLine.getOptionValue("apikey");
        _initialize(apikey);
        uploadFaces(addr,apikey);
        checkUploadStatus(timeout,includeTime);
    }

    //Initializes the API Client and Webservices
    private static void _initialize(final String apiKey)
    {
        {
            final ClientBuilder clientBuilder = ClientBuilder.newBuilder();
            clientBuilder.register(new JacksonJaxbJsonProvider().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));;

            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            clientBuilder.hostnameVerifier(hostnameVerifier);

            _apiClient = new ApiClient();
            _apiClient.setHttpClient(clientBuilder.build());
            _apiClient.addDefaultHeader("x-auth-scheme", "api-token");
            _apiClient.addDefaultHeader("x-auth-apikey", apiKey);

            _faceWebService = new FaceWebserviceApi(_apiClient);
            _uploadWebService = new UploadWebserviceApi(_apiClient);
        }
    }

    private static void uploadFaces(String apikey) throws Exception
    {
        

        MultiPart multiPart = new MultiPart();
        List<BodyPart> bodyParts = new ArrayList<BodyPart>();
        multiPart.setBodyParts(bodyParts);

        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("x-auth-scheme", "api-token"));
        defaultHeaders.add(new BasicHeader("x-auth-apikey", apikey));

        _uploadWebService.uploadFaces(null,null,null,null,null,null,null,null,null,null);
    }

    private static FaceGetUploadedFacesWSResponse getUploadedFaces() throws Exception
    {
        final FaceGetUploadedFacesWSRequest uploadedFacesRequest = new FaceGetUploadedFacesWSRequest();
        final FaceGetUploadedFacesWSResponse uploadedFacesResponse = _faceWebService.getUploadedFaceMetadata(uploadedFacesRequest);
        return uploadedFacesResponse;
    }


    private static void uploadFaces(String address, String apikey)
    {

    }
    private static void checkUploadStatus(int timeout,boolean includeTime)
    {

    }
}