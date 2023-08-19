package digital.ivan.lightchain.starter.pinecone.model;

import java.util.List;

public class PineconeResponse {
    private List<Match> matches;
    private String namespace;

    public List<Match> getMatches() {
        return matches;
    }

    public static class Match {
        private String id;
        private double score;
        private Metadata metadata;

        public String getId() {
            return id;
        }

        public double getScore() {
            return score;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public static class Metadata {
            private String city;
            private String country;
            private String info;

            public String getInfo() {
                return info;
            }
        }
    }
}
