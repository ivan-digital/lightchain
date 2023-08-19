package digital.ivan.lightchain.starter.openai.model;

import java.util.List;

public class ModelsList {

    public ModelsList() {}
    public ModelsList(List<Model> data) {
        this.data = data;
    }
    private List<Model> data;

    public List<Model> getData() {
        return data;
    }

    public static class Model {
        private String id;
        private String object;
        private String ownedBy;
        private List<String> permission;

        public Model() {}
        public Model(String id, String object, String ownedBy, List<String> permission) {
            this.id = id;
            this.object = object;
            this.ownedBy = ownedBy;
            this.permission = permission;
        }

        public String getId() {
            return id;
        }

        public String gx3etObject() {
            return object;
        }

        public String getOwnedBy() {
            return ownedBy;
        }

        public List<String> getPermission() {
            return permission;
        }
    }
}
