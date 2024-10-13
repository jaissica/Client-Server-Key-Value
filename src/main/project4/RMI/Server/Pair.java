package main.project4.RMI.Server;

/**
 * Pair class represents a simple key value pair
 *
 * @param <K> type of the key
 * @param <T> type of the value
 */
class Pair<K, T> {

  public T value;
  public K key;

  /**
   * This method constructs Pair object with the specified key and value
   *
   * @param key   key of the pair
   * @param value value associated with the key
   */
  Pair(K key, T value) {
    this.key = key;
    this.value = value;
  }

  /**
   * This method helps in getting the value associated with the key
   *
   * @return value associated with the key
   */
  public T getValue() {
    return value;
  }

  /**
   * This method sets the value associated with the key
   *
   * @param value new value to be associated with the key
   */
  public void setValue(T value) {
    this.value = value;
  }

  /**
   * This method gets the key of the pair
   *
   * @return key of the pair
   */
  public K getKey() {
    return key;
  }

  /**
   * This method sets the key of the pair
   *
   * @param key new key for the pair
   */
  public void setKey(K key) {
    this.key = key;
  }
}
