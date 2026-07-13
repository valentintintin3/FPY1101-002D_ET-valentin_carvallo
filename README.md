[VALENTIN_CARVALLO_002D.py](https://github.com/user-attachments/files/29983368/VALENTIN_CARVALLO_002D.py)
# FPY1101-002D_ET-valentin_carvallo

Evaluación Final Transversal

def Leer_opcion():
    while True:
        try:
            opcion_seleccionada = int(input("Ingrese una opción: "))
            if 1 <= opcion_seleccionada <= 6:
                return opcion_seleccionada
            else:
                print("Debe seleccionar una opción valida.")
        except:
            print("Debe seleccionar una opción valida.")

def cupos_genero(genero_a_buscar, diccionario_peliculas, diccionario_cartelera):
    total_cupos_acumulados = 0
    genero_busqueda_mayusculas = genero_a_buscar.upper()
    for codigo_pelicula in diccionario_peliculas:
        genero_pelicula_actual = diccionario_peliculas[codigo_pelicula][1].upper()
        if genero_pelicula_actual == genero_busqueda_mayusculas:
            cupos_disponibles = diccionario_cartelera[codigo_pelicula][1]
            total_cupos_acumulados = total_cupos_acumulados + cupos_disponibles
    print(f"El total de cupos disponibles es : {total_cupos_acumulados}")

def actualizar_precio(codigo_pelicula, nuevo_precio_asignar, diccionario_cartelera):
    codigo_mayusculas = codigo_pelicula.upper()
    if codigo_mayusculas in diccionario_cartelera:
        diccionario_cartelera[codigo_mayusculas][0] =nuevo_precio_asignar
        return True
    return False

def busqueda_precio(precio_minimo, precio_maximo, diccionario_cartelera, diccionario_peliculas):
  lista_peliculas_filtradas = []
  for codigo_pelicula in diccionario_cartelera:
      precio_actual = diccionario_cartelera[codigo_pelicula][0]
      cupos_actuales = diccionario_cartelera[codigo_pelicula][1]

      if precio_minimo <= precio_actual <= precio_maximo and cupos_actuales > 0:
          titulo_pelicula = diccionario_peliculas[codigo_pelicula][0]
          texto_combinado = f"{titulo_pelicula}-{codigo_pelicula}"
          lista_peliculas_filtradas.append(texto_combinado)
  if len(lista_peliculas_filtradas) > 0:
        lista_peliculas_filtradas.sort()
        for elemento_pelicula in lista_peliculas_filtradas:
            print(elemento_pelicula)
  else:
    print("No hay peliculas en ese rango de precios.")

def validar_codigo(codigo_a_validar):
    if codigo_a_validar == "": return False
    return True

def validar_titulo(titulo_a_validar):
    if titulo_a_validar == "": return False
    return True

def validar_genero(genero_a_validar):
    if genero_a_validar == "": return False
    return True

def validar_duracion(duracion_a_validar):
    try:
        if int(duracion_a_validar) > 0: return True
        return False
    except:
        return False
    
def validar_clasificacion(clasificacion_a_validar):
    if clasificacion_a_validar in ["A", "B", "C"]: return True
    return False

def validar_idioma(idioma_a_validar):
    if idioma_a_validar == "": return False
    return True

def validar_es_3d(es_3d_a_validar):
    if es_3d_a_validar in ["s", "n"]: return True
    return False

def validar_precio(precio_a_validar):
    try:
        if int(precio_a_validar) > 0: return True
        return False
    except:
        return False
    
def validar_cupos(cupos_a_validar):
    try:
        if int(cupos_a_validar) >= 0: return True
        return False
    except:
        return False
    
def agregar_pelicula(codigo, titulo, genero, duracion, clasificacion, idioma, es_3d, precio, cupos, diccionario_peliculas, diccionario_cartelera):
    codigo_mayusculas = codigo.upper()
    if codigo_mayusculas in diccionario_peliculas:
        return False
    valor_booleano_3d = True if es_3d == "s" else False
    diccionario_peliculas[codigo_mayusculas] = [titulo, genero, int(duracion), clasificacion, idioma, valor_booleano_3d]
    diccionario_cartelera[codigo_mayusculas] = [int(precio), int(cupos)]
    return True

def eliminar_pelicula(codigo_eliminar, diccionario_peliculas, diccionario_cartelera):
    codigo_mayusculas = codigo_eliminar.upper()
    if codigo_mayusculas in diccionario_peliculas:
        diccionario_peliculas.pop(codigo_mayusculas)
        diccionario_cartelera.pop(codigo_mayusculas)
        return True
    return False


peliculas = {
'P101': ['Luz de Otoño', 'drama', 110, 'B', 'Español', False],
'P102': ['Noche Neón', 'acción', 125, 'C', 'Ingles', True],
'P103': ['Planeta Agua', 'documental', 90, 'A', 'Español',
False],
'P104': ['Risa Total', 'comedia', 105, 'A', 'Español', True],
'P105': ['Código Zero', 'thriller', 118, 'C', 'Ingles', True],
'P106': ['Viaje Lunar', 'ciencia ficción', 132, 'B', 'Ingles',
False],
}
cartelera = {
'P101': [5990, 40],
'P102': [7990, 0],
'P103': [4990, 25],
'P104': [6990, 12],
'P105': [8990, 8],
'P106': [7490, 3],
}


while True:
    print("-----------MENÚ PRINCIPAL------------")
    print("(1). Cupos por género")
    print("(2). Busqueda de peliculas por rango de precio")
    print("(3). Actualizar precio de pelicula")
    print("(4). Agregar pelicula")
    print("(5). Eliminar pelicula")
    print("(6). Salir")
    print("-------------------------------------")

    opcion = Leer_opcion()
    if opcion == 1:
        genro_ingresado = input("ingrese genero a consultar: ")
        cupos_genero(genro_ingresado, peliculas, cartelera)
    elif opcion == 2:
            try:
                precio_minimo_ingresado = int(input("Ingrese precio minimo: "))
                precio_maximo_ingresado = int(input("Ingrese precio maximo: "))
                if precio_minimo_ingresado >= 0 and precio_maximo_ingresado >= 0 and precio_minimo_ingresado <= precio_maximo_ingresado:
                    busqueda_precio(precio_minimo_ingresado, precio_maximo_ingresado, cartelera, peliculas)

                
                else:
                    print("Debe ingresar valores enteros. El precio minimo y maximo deben ser mayores que o iguales a 0 y el precio minimo meno o igual al precio maximo.")
            except:
                print("Debe ingresar valores enteros. El precio minimo y maximo deben ser mayores que o iguales a 0 y el precio minimo meno o igual al precio maximo.")
    elif opcion == 3:
        while True:
            codigo_pelicula_actualizar = input("Ingrese codigo de la pelicula: ")

            while True:
                try:
                    nuevo_precio_ingresado = int(input("Ingrese nuevo precio: "))
                    if nuevo_precio_ingresado > 0:
                        break
                    else:
                        print("Error, el precio debe ser un entero positivo.")
                except:
                    print("Error, el precio debe ser un entero positivo.")
            if actualizar_precio(codigo_pelicula_actualizar, nuevo_precio_ingresado, cartelera):
                print("Precio actualizado.")
            else:
                print("El codigo no existe.")
            respuesta_usuario = input("¿Desea actualizar otro precio (s/n)?: ")
            if respuesta_usuario != "s":
                break

    elif opcion == 4:
        solicitar_codigo = input("Ingrese codigo de la pelicula: ")
        solicitar_titulo = input("Ingrese titulo: ")
        solicitar_genero = input("Ingrese genero: ")
        solicitar_duracion = input("Ingrese duración en minutos: ")
        solicitar_clasificacion= input("Ingrese clasificación (A/B/C): ")
        solicitar_idioma = input("Ingrese idioma: ")
        solicitar_es_3d = input("¿Es formato 3D? (s/n): ")
        solicitar_precio = input("Ingrese precio de la entrada: ")
        solicitar_stock_cupos = input("Ingrese cantidad de cupos disponibles: ")
        if (validar_codigo(solicitar_codigo) and validar_titulo(solicitar_titulo) and validar_genero(solicitar_genero) and validar_duracion(solicitar_duracion) and validar_clasificacion(solicitar_clasificacion) and validar_idioma(solicitar_idioma) and validar_es_3d(solicitar_es_3d) and validar_precio(solicitar_precio) and validar_cupos(solicitar_stock_cupos)):
            
            if solicitar_codigo.upper() in peliculas:
                print("Elcodigo ya existe.")
            else:
                agregar_pelicula(solicitar_codigo, solicitar_titulo, solicitar_genero, solicitar_duracion, solicitar_clasificacion, solicitar_idioma, solicitar_es_3d, solicitar_precio, solicitar_stock_cupos, peliculas, cartelera)
                print("Pelicula agregada.")

        else:
            print("Error: Uno o más datos no cumplen con las condiciones de validación.")

    elif opcion == 5:
        codigo_pelicula_eliminar =input("Ingrese el codigo de la pelicula que desea eliminar: ")
        if codigo_pelicula_eliminar.upper() in peliculas:
            eliminar_pelicula(codigo_pelicula_eliminar, peliculas, cartelera)
            print("Pelicula eliminada.")
        else:
            print("El codigo no existe.")
    
    elif opcion == 6:
        print("Programa finalizado.")
        break



        

