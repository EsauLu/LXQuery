package esau.lxq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqClientImpl;
import esau.lxq.net.impl.LxqRequestImpl;

public class ClientManager {

    private String[] serverIps = { 
            "192.168.118.128", 
            "192.168.118.129", 
//            "192.168.118.130", 
//            "192.168.118.131",
//            "192.168.118.132", 
//            "192.168.118.133", 
//            "192.168.118.134", 
//            "192.168.118.135", 
            };

    private int port = 29000;

    private Map<Integer, LxqClient> clientsMap;

    public ClientManager() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void initClients(List<Integer> pids) {

        pids = new ArrayList<Integer>();
        clientsMap = new HashMap<>();

        for (int i = 0; i < serverIps.length; i++) {

            pids.add(i);

            LxqClient client = new LxqClientImpl(serverIps[i], port);

            clientsMap.put(i, client);

        }

    }

    public LxqClient getClientByPid(int pid) {
        return clientsMap.get(pid);
    }

    public List<LxqClient> getClients() {
        List<LxqClient> clients = new ArrayList<>();
        clients.addAll(clientsMap.values());
        return clients;
    }

    public Map<Integer, LxqClient> getClientsMap() {
        return clientsMap;
    }

    public List<LxqResponse> sendChunks(List<Integer> pids, List<String> chunks) {

        int p = pids.size();

        List<LxqResponse> responses = new ArrayList<>();

        for (int i = 0; i < p; i++) {

            Integer pid = pids.get(i);
            String chunk = chunks.get(i);
            LxqClient client = clientsMap.get(pid);

            LxqResponse response = sendChunk(client, chunk);
            responses.add(response);

        }

        return responses;

    }

    public LxqResponse sendChunk(LxqClient client, String chunk) {

        LxqRequest request = new LxqRequestImpl();

        request.setCode(LxqRequest.CHUNK);

        request.setChunk(chunk);

        client.execute(request);

        return client.getResponse();
    }
    
    public List<LxqResponse> getResponses(List<Integer> pids){
        
        int p = pids.size();

        List<LxqResponse> responses = new ArrayList<>();
        
        for(int i=0;i<p;i++){
            int pid=pids.get(i);
            responses.add(getResponse(pid));
        }
        
        return responses;
        
    }
    
    public LxqResponse getResponse(Integer pid){
        LxqClient client=clientsMap.get(pid);
        return client.getResponse();        
    }

}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
