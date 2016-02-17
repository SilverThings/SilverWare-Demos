/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2010 - 2013 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.silverware.demos.devconf.kjar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class BatchLightCommand extends Command {

   private List<LightCommand> commands = new ArrayList<>();

   public BatchLightCommand(final LightCommand... commands) {
      this.commands.addAll(Arrays.asList(commands));
   }

   public BatchLightCommand(final List<LightCommand> commands) {
      this.commands.addAll(commands);
   }

   public BatchLightCommand() {

   }

   public void addCommand(final LightCommand command) {
      commands.add(command);
   }

   public List<LightCommand> getCommands() {
      return Collections.unmodifiableList(commands);
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final BatchLightCommand that = (BatchLightCommand) o;

      return commands.equals(that.commands);

   }

   @Override
   public int hashCode() {
      return commands.hashCode();
   }

   @Override
   public String toString() {
      return "BatchLightCommand{" +
            "commands=" + commands +
            '}';
   }

   public String getBatch() {
      final StringBuilder sb = new StringBuilder();

      commands.forEach(l -> {
         sb.append(l.getPlace().getLed());
         sb.append(";r;");
         sb.append(l.getState().getR());
         sb.append("\n");
         sb.append(l.getPlace().getLed());
         sb.append(";g;");
         sb.append(l.getState().getG());
         sb.append("\n");
         sb.append(l.getPlace().getLed());
         sb.append(";b;");
         sb.append(l.getState().getB());
         sb.append("\n");
      });

      return sb.toString();
   }
}
