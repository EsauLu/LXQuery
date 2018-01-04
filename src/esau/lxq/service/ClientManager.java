package esau.lxq.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esau.lxq.entry.Node;
import esau.lxq.entry.PNode;
import esau.lxq.net.LxqClient;
import esau.lxq.net.LxqRequest;
import esau.lxq.net.LxqResponse;
import esau.lxq.net.impl.LxqClientImpl;
import esau.lxq.net.impl.LxqRequestImpl;

public class ClientManager {

    private int clientNum;

    private String[] serverIps;

    private int port;

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

    public void sendChunks(String path, List<Integer> pids) {

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return;
        }

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.CHUNK);

        InputStream in = null;

        try {

            in = new FileInputStream(file);
            request.setInputStream(in);

            long size = file.length() / clientNum + 1;
            for (int i = 0; i < clientNum; i++) {

                int pid = pids.get(i);
                LxqClient client = clientsMap.get(pid);

                request.setMsg(String.valueOf(i));
                if (i == 0) {
                    request.setChunk("<root>");
                } else if (i == clientNum - 1) {
                    request.setChunk("</root>");
                } else {
                    request.setChunk(null);
                }

                client.commit(request, size);

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }

    }

    public void finxChunks(List<Integer> pids, List<String> chunks) {

        LxqRequest request = new LxqRequestImpl();
        request.setCode(LxqRequest.FILL_CHUNK);

        for (int i = 0; i < clientNum; i++) {
            int pid = pids.get(i);
            LxqClient client = getClientByPid(pid);
            String chunk = chunks.get(i + 1);
            request.setChunk(chunk);
            client.commit(request);
        }

    }

    public void sendChunk(int pid, String chunk) {

        LxqRequest request = new LxqRequestImpl();

        LxqClient client = clientsMap.get(pid);

        request.setCode(LxqRequest.CHUNK);

        request.setMsg(String.valueOf(pid));

        request.setChunk(chunk);

        client.commit(request);

    }

    public LxqResponse sendRequestByLock(int pid, LxqRequest request) {        
        sendRequest(pid, request);        
        LxqResponse response=getResponse(pid);        
        return response;
    }

    public void sendRequest(int pid, LxqRequest request) {
        LxqClient client = clientsMap.get(pid);
        client.commit(request);
    }

    public void sendRequests(LxqRequest request) {
        for (Integer pid : clientsMap.keySet()) {
            sendRequest(pid, request);
        }
    }

    public void sendRequests(Map<Integer, LxqRequest> requests) {
        for (Integer pid : requests.keySet()) {
            sendRequest(pid, requests.get(pid));
        }
    }

    public Map<Integer, LxqResponse> getResponseMap(List<Integer> pids) {

        int p = pids.size();

        Map<Integer, LxqResponse> responses = new HashMap<>();

        for (int i = 0; i < p; i++) {
            int pid = pids.get(i);
            responses.put(pid, getResponse(pid));
        }

        return responses;

    }

    public List<List<Node>> getNodeResultLists(List<Integer> pids) {
        int p = pids.size();

        List<List<Node>> responses = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            int pid = pids.get(i);
            LxqResponse response = getResponse(pid);
            List<Node> result = response.getNodeList();
            responses.add(result);
        }

        return responses;

    }
    public List<List<PNode>> getPNodeResultLists(List<Integer> pids) {
        int p = pids.size();

        List<List<PNode>> responses = new ArrayList<>();

        for (int i = 0; i < p; i++) {
            int pid = pids.get(i);
            LxqResponse response = getResponse(pid);
            List<PNode> result = response.getPNodeList();
            responses.add(result);
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
