package com.iutbm.monumentdroid.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iutbm.monumentdroid.activity.R;
import com.iutbm.monumentdroid.models.Monument;

public class AdapterListMonuments extends BaseAdapter {

	private ArrayList<Monument> monuments;
	private LayoutInflater myInflater;

	
	/**
	 * Constructeur par défaut
	 * @param _context
	 * @param _monuments
	 */
	public AdapterListMonuments (Context _context, ArrayList<Monument> _monuments)
	{
		this.myInflater = LayoutInflater.from(_context);
		this.monuments = _monuments;
	}
	

	/**
	 * Retourne le nombre de monuments
	 */
	public int getCount() {
		return this.monuments.size();
	}

	/**
	 * Retourne le monuments à l'indice position de la listView
	 * @param position
	 */
	public Monument getItem(int position) {
		return this.monuments.get(position);
	}

	/**
	 * Retourne le numéro du monuments à la position position
	 */
	public long getItemId(int position) {
		return this.monuments.get(position).getId();
	}

	/**
	 *  Classe permettant d'afficher des information sur une les vues de la liste
	 * @author julienpetit
	 *
	 */
	public static class ViewHolder {
		TextView nom;
		TextView date;
	}
	
	/**
	 * Retourne une Vue 
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		// Parseur de la date de long à texte 
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = myInflater.inflate(R.layout.itemlistmonuments, null);
			holder = new ViewHolder();
			
			
			holder.nom = (TextView) convertView.findViewById(R.itemlistmonument.libelle);
			holder.date = (TextView) convertView.findViewById(R.itemlistmonument.date);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// affichage du texte dans les vues
		holder.nom.setText(monuments.get(position).getLibelle());
		holder.date.setText(format.format(new Date(monuments.get(position).getLocation().getTime())));
		
		return convertView;
	}
}
