package enums;

public enum UNIT_CLASS_TYPE {

    KNIGHT {
        @Override
        public String getDescription() {
            return "Knight";
        }

        @Override
        public long getCode() {
            return 1;
        }


    },
    LANCER{
        @Override
        public String getDescription() {
            return "Lancer";
        }

        @Override
        public long getCode() {
            return 2;
        }
    },
    ARCHER {
        @Override
        public String getDescription() {
            return "Archer";
        }

        @Override
        public long getCode() {
            return 3;
        }
    };

    public abstract String getDescription();
    public abstract long getCode();
}
