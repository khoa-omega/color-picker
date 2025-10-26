package io.github.komega.colorpicker.component

//.drawWithCache {
//                            val thickness = shape.thickness.toPx()
//                            val diameter = size.minDimension - thickness
//                            val extend = thickness * 180f / (diameter * PI).toFloat()
//                            val brush = Brush.sweepGradient(
//                                0f to Color.White,
//                                extend / 360f to Color.White,
//                                abs(shape.sweep) / 360f to Color.Black,
//                                (abs(shape.sweep) + extend) / 360f to Color.Black
//                            ) as ShaderBrush
//                            brush.transform = Matrix().apply {
//                                resetToPivotedTransform(
//                                    pivotX = size.width * 0.5f,
//                                    pivotY = size.height * 0.5f,
//                                    rotationZ = (if (shape.sweep > 0) shape.start else shape.end )- extend
//                                )
//                            }
//                            val outline = shape.createOutline(size, layoutDirection, this)
//                            onDrawBehind {
//                                drawOutline(outline, brush)
//                            }
//                        }
