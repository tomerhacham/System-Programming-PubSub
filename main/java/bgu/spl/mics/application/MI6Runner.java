package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.M;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        List<Thread> threads = new LinkedList<Thread>();

        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(args[0]));
        } catch (IOException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject json = (JSONObject) obj;
        JSONArray inv = (JSONArray) json.get("inventory");
        String[] s = MI6Runner.ConvertJSONArrayToStringArray(inv);

        //Create Inventory
        Inventory.getInstance().load(s);
        Inventory.getInstance().printToFile("inventory");

        JSONObject services = (JSONObject) json.get("services");
        //Create TimeService
        Long time = (Long) services.get("time");
        TimeService timeService = new TimeService(time.intValue());
        Thread time_service_thread = new Thread(timeService);
        threads.add(time_service_thread);

        //Create M and Moneypenny
        Long Moneypenny =(Long)services.get("Moneypenny");
        Long M = (Long)services.get("M");
        for(int i=0;i<Moneypenny;i++){
            Moneypenny moneypenny= new Moneypenny(i+1);
            Thread moneypenny_thread = new Thread(moneypenny);
            threads.add(moneypenny_thread);
        }

        for(int i=0;i<M;i++){
            M m= new M(i+1,time.intValue());
            Thread m_thread = new Thread(m);
            threads.add(m_thread);
        }


        //Create all intelligence sources
        JSONArray intelligence = (JSONArray) services.get("intelligence");
        //List<Intelligence> IntelSources = new LinkedList<>();
        for (Object _intelsource:intelligence ) {
            JSONObject intelsource = (JSONObject)_intelsource;
            JSONArray missions = (JSONArray) intelsource.get("missions");
            Intelligence intelligence1 = new Intelligence(MI6Runner.createMissions(missions));
            Thread intel_thread = new Thread(intelligence1);
            threads.add(intel_thread);
        }

        //Create Squad
        Squad squad = Squad.getInstance();
        JSONArray sqd = (JSONArray)json.get("squad");
        Agent[] agents = new Agent[sqd.size()];
        int i=0;
        for (Object _agent:sqd){
            JSONObject jsonAgent = (JSONObject)_agent;
            Agent agent = new Agent((String)jsonAgent.get("serialNumber"),(String)jsonAgent.get("name"));
            agents[i]=agent;
            i++;
        }
        squad.load(agents);

        //Initialize all threads
        for (Thread thread:threads) {
            thread.run();
        }
    }

    private static List<MissionInfo> createMissions(JSONArray info){
        LinkedList<MissionInfo> list = new LinkedList<>();
        for (Object missionInfo:info ) {
            JSONObject jsonObject = (JSONObject)missionInfo;
            MissionInfo mission = new MissionInfo();
            Long duration = (Long) jsonObject.get("duration");
            Long timeExpired = (Long) jsonObject.get("timeExpired");
            Long timeIssued = (Long) jsonObject.get("timeIssued");
            String gadget = (String) jsonObject.get("gadget");
            String missionName = (String) jsonObject.get("missionName");
            JSONArray serialsJsonArray = (JSONArray)jsonObject.get("serialAgentsNumbers");
            LinkedList<String> sn = new LinkedList<>();
            for (Object snobject:serialsJsonArray) {
                sn.add((String)snobject);
            }
            list.add(MI6Runner.setMissionInfo(gadget,duration.intValue(),timeExpired.intValue(),timeIssued.intValue(),missionName,sn));
        }
        return list;
    }

    private static String[] ConvertJSONArrayToStringArray(JSONArray jsonArray){
        String[] strings = new String[jsonArray.size()];
        for(int i=0;i<jsonArray.size();i++){
            strings[i] = (String) jsonArray.get(i);
        }
        return strings;
    }

    private static MissionInfo setMissionInfo(String gadget,int duration,int timeExpired, int timeIssued,String missionName,List<String> serialAgentsNumbers){
        MissionInfo missionInfo = new MissionInfo();
        missionInfo.setSerialAgentsNumbers(serialAgentsNumbers);
        missionInfo.setDuration(duration);
        missionInfo.setGadget(gadget);
        missionInfo.setTimeIssued(timeIssued);
        missionInfo.setTimeExpired(timeExpired);
        missionInfo.setMissionName(missionName);
        return  missionInfo;
    }
}
