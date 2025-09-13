package common.utility;

import common.entity.Worker;
import java.io.Serializable;
import java.util.List;

// из команды
public record Response(String message, List<Worker> workers, int returnCode)
        implements Serializable {}
