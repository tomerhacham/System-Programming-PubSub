package bgu.spl.mics.application.messages;

import java.util.List;

    public class SendAgentsEvent {
        //fields:
        private List<String> serialAgentsNumbers;
        //constructors:
        public SendAgentsEvent(List<String> serialAgentsNumbers){
            this.serialAgentsNumbers=serialAgentsNumbers;
        }
        //methods:
        public List<String> GetSerialAgentsNumbers(){
            return this.serialAgentsNumbers;
        }
}