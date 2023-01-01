/*
 * Copyright (c) 2022. StulSoft
 */

package com.stulsoft.ys.cats.effect.resource

import cats.effect._
import cats.effect.{IO, IOApp, Resource}
import cats.syntax.all._
import com.typesafe.scalalogging.StrictLogging

import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}

object CopyFiles extends IOApp, StrictLogging:
  private def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO.blocking(new FileInputStream(f)) // build
    } { inStream =>
      IO.blocking(inStream.close()).handleErrorWith(_ => IO.unit) // release
    }

  private def outputStream(f: File): Resource[IO, FileOutputStream] =
    Resource.make {
      IO.blocking(new FileOutputStream(f)) // build
    } { outStream =>
      IO.blocking(outStream.close()).handleErrorWith(_ => IO.unit) // release
    }

  private def inputOutputStreams(in: File, out: File): Resource[IO, (InputStream, OutputStream)] =
    for {
      inStream <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  private def transmit(origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): IO[Long] =
    for {
      amount <- IO.blocking(origin.read(buffer, 0, buffer.length))
      count <- if (amount > -1) IO.blocking(destination.write(buffer, 0, amount))
        >> transmit(origin, destination, buffer, acc + amount)
      else IO.pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count // Returns the actual amount of bytes transmitted // Returns the actual amount of bytes transmitted

  // transfer will do the real work
  private def transfer(origin: InputStream, destination: OutputStream): IO[Long] =
    transmit(origin, destination, new Array[Byte](1024 * 10), 0L)

  private def copy(origin: File, destination: File): IO[Long] =
    inputOutputStreams(origin, destination).use { case (in, out) =>
      transfer(in, out)
    }

  override def run(args: List[String]): IO[ExitCode] =
    logger.info("==>run")
    val orig = new File("README.adoc")
    val dest = new File("c:/work/README.adoc")
    for {
      count <- copy(orig, dest)
      _ <- IO.println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}")
    } yield ExitCode.Success

