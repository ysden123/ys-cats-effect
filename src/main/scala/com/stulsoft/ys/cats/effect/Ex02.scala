/*
 * Copyright (c) 2023. StulSoft
 */

package com.stulsoft.ys.cats.effect

import cats.effect.IO
import cats.effect.unsafe.implicits.global

import com.typesafe.scalalogging.StrictLogging

/**
 * See [[https://yadukrishnan.live/cats-effect-3-for-beginners-part-1]]
 */
object Ex02 extends StrictLogging:
  private val scalaIO = IO(logger.info("Welcome to Cats Effect 3"))
  private val scalaIO2 = IO(logger.info("222"))
  def main(args: Array[String]): Unit = {
    logger.info("==>main")
    scalaIO.unsafeRunSync()
    scalaIO2.unsafeRunSync()
  }
