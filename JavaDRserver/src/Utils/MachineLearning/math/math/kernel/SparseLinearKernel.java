/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package Utils.MachineLearning.math.math.kernel;

import Utils.MachineLearning.math.math.Math;
import Utils.MachineLearning.math.math.kernel.MercerKernel;
import Utils.MachineLearning.math.math.SparseArray;

/**
 * The linear dot product kernel on sparse arrays. When using a linear kernel, input space is
 * identical to feature space.
 *
 * @author Haifeng Li
 */
public class SparseLinearKernel implements MercerKernel<SparseArray> {

    /**
     * Constructor.
     */
    public SparseLinearKernel() {
    }

    @Override
    public String toString() {
        return "Sparse Linear Kernel";
    }

    public double k(SparseArray x, SparseArray y) {        
        return Math.dot(x, y);
    }
}
