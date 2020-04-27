package com.github.nicholasmoser.iso;

/**
 * An item in the ISO. This can be either a file, directory, or root.
 */
public interface ISOItem {

  /**
   * @return The parent game path of this ISOItem.
   */
  String getParent();

  /**
   * @return If this ISOItem is a directory.
   */
  boolean isDirectory();

  /**
   * @return If this ISOItem is root.
   */
  boolean isRoot();

  /**
   * @return The name of this ISOItem.
   */
  String getName();

  /**
   * @return The relative game path of this ISOItem.
   */
  String getGamePath();
}
