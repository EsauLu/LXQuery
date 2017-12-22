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

    private int clientNum;

//    private String[] serverIps = { "192.168.118.128", "192.168.118.129", "192.168.118.130", "192.168.118.131",
//            "192.168.118.132", "192.168.118.133", "192.168.118.134", "192.168.118.135", };

    private String[] serverIps = { 
            "172.21.52.50", 
            "172.21.52.51", 
            "172.21.52.52", 
            "172.21.52.53", 
            "172.21.52.54", 
            "172.21.52.55", 
            "172.21.52.56", 
            "172.21.52.57", 
        };

    private int port = 29000;

    private Map<Integer, LxqClient> clientsMap;

    public ClientManager() {
        // TODO Auto-generated constructor stub
    }

    public ClientManager(String[] serverIps, int port) {
        this.serverIps = serverIps;
        this.port = port;
    }

    public void initClients(List<Integer> pids) {

        clientNum = pids.size();

        clientsMap = new HashMap<>();

        for (int i = 0; i < clientNum; i++) {

            int pid = pids.get(i);

            LxqClient client = new LxqClientImpl(serverIps[i], port);

            clientsMap.put(pid, client);

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

//    public void sendChunks(List<Integer> pids, List<String> chunks) {
//
//        int p = pids.size();
//
//        for (int i = 0; i < p; i++) {
//
//            Integer pid = pids.get(i);
//            String chunk = chunks.get(i);
//
//            sendChunk(pid, chunk);
//
//        }
//        
//        getResponseList(pids);
//    }

    public void sendChunk(int pid, String chunk) {
        
        System.out.println("chunk --> "+pid);

        LxqRequest request = new LxqRequestImpl();
        
        LxqClient client=clientsMap.get(pid);

        request.setCode(LxqRequest.CHUNK);
        
        request.setMsg(String.valueOf(pid));

        request.setChunk(chunk);

        client.execute(request);
        
    }
    
    public LxqResponse sendRequestByLock(int pid, LxqRequest request){
        sendRequest(pid, request);
        return getResponse(pid);
    }
    
    public void sendRequest(int pid, LxqRequest request){
        LxqClient client=clientsMap.get(pid);
        client.execute(request);        
    }
    
    public void sendRequests(LxqRequest request){
        for(Integer pid: clientsMap.keySet()){
            sendRequest(pid, request);
        }
    }
    
    public void sendRequests(Map<Integer, LxqRequest> requests){
        for(Integer pid: requests.keySet()){
            sendRequest(pid, requests.get(pid));
        }
    }
    
    public Map<Integer, LxqResponse> getResponseMap(List<Integer> pids){

        int p = pids.size();

        Map<Integer, LxqResponse> responses = new HashMap<>();

        for (int i = 0; i < p; i++) {
            int pid = pids.get(i);
            responses.put(pid, getResponse(pid));
        }

        return responses;

    }

    public List<LxqResponse> getResponseList(List<Integer> pids) {

        int p = pids.size();

        List<LxqResponse> responses = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            int pid = pids.get(i);
            responses.add(getResponse(pid));
        }

        return responses;

    }

    public LxqResponse getResponse(Integer pid) {
        LxqClient client = clientsMap.get(pid);
        LxqResponse response = client.getResponse();
        return response;
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
