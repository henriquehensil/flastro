package codes.shawlas.data.exception.table.shard;

public class InvalidTableException extends ShardingException {
    public InvalidTableException(String message) {
        super(message);
    }
}
